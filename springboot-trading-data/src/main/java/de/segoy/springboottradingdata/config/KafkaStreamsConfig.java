package de.segoy.springboottradingdata.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.segoy.springboottradingdata.kafkastreams.OptionsContractDataCombineService;
import de.segoy.springboottradingdata.model.data.entity.PositionData;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.state.KeyValueStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;
import org.springframework.kafka.config.KafkaStreamsConfiguration;
import org.springframework.kafka.support.serializer.JsonSerde;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafkaStreams
@RequiredArgsConstructor
public class KafkaStreamsConfig {

    private final KafkaConstantsConfig kafkaConstantsConfig;
    private final OptionsContractDataCombineService optionsContractDataCombineService;

    @Bean(name= KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME)
    KafkaStreamsConfiguration kafkaStreamsConfiguration(){
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConstantsConfig.getBOOTSTRAP_SERVERS());
        configProps.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        configProps.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, JsonSerde.class);

        configProps.put(StreamsConfig.APPLICATION_ID_CONFIG, "trading-data-processing-app");

        return new KafkaStreamsConfiguration(configProps);
    }


    @Bean
    public Topology processOptionsContractData(StreamsBuilder streamsBuilder){
        ObjectMapper mapper = new ObjectMapper();
        Serde<PositionData> positionDataSerde = new JsonSerde<>(PositionData.class, mapper);

        final Consumed<String, PositionData> consumed = Consumed.with(Serdes.String(), positionDataSerde);
        final KStream<String, PositionData> positions =
                streamsBuilder.stream(kafkaConstantsConfig.getOPTION_POSITIONS_TOPIC(), consumed);

        KTable<String, PositionData> sortByTradingClassAndLastTradeDate =
                positions
                        .selectKey((key, value)-> value.getContractData().getLastTradeDate()+" "+value.getContractData().getTradingClass())
                        .groupByKey()
                        .aggregate(
                                ()-> PositionData.builder().build(),
                                (key, newPos, aggregatedPos) ->optionsContractDataCombineService.combinePositions(newPos, aggregatedPos),
                                Materialized.<String, PositionData, KeyValueStore<Bytes, byte[]>>as(kafkaConstantsConfig.getPOSITIONS_AGGREGATE_TOPIC())
                                        .withKeySerde(Serdes.String())
                                        .withValueSerde(positionDataSerde)
                        );

        sortByTradingClassAndLastTradeDate.toStream().to(kafkaConstantsConfig.getPOSITION_TOPIC());
        return streamsBuilder.build();
    }
}
