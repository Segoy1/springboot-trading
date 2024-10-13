package de.segoy.springboottradingdata.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.segoy.springboottradingdata.constants.AutoDayTradeConstants;
import de.segoy.springboottradingdata.kafkastreams.StreamOptionChainDataCreator;
import de.segoy.springboottradingdata.kafkastreams.StreamOptionsContractDataCombineService;
import de.segoy.springboottradingdata.model.data.entity.PositionDbo;
import de.segoy.springboottradingdata.model.data.kafka.OptionChainData;
import de.segoy.springboottradingdata.model.data.kafka.OptionMarketData;
import java.util.*;
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

@Configuration
@EnableKafkaStreams
@RequiredArgsConstructor
public class KafkaStreamsConfig {

  private final KafkaConstantsConfig kafkaConstantsConfig;
  private final StreamOptionsContractDataCombineService streamOptionsContractDataCombineService;
  private final StreamOptionChainDataCreator streamOptionChainDataCreator;

  @Bean(name = KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME)
  KafkaStreamsConfiguration kafkaStreamsConfiguration() {
    Map<String, Object> configProps = new HashMap<>();
    configProps.put(
        StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConstantsConfig.getBOOTSTRAP_SERVERS());
    configProps.put(
        StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
    configProps.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, JsonSerde.class);
    configProps.put(StreamsConfig.PROCESSING_GUARANTEE_CONFIG, StreamsConfig.EXACTLY_ONCE_V2);

    String stateDir = System.getProperty("user.dir") + "/../tmp/kafka-streams-state";
    configProps.put(StreamsConfig.STATE_DIR_CONFIG,stateDir);

    configProps.put(StreamsConfig.APPLICATION_ID_CONFIG, "trading-data-process-app");

    return new KafkaStreamsConfiguration(configProps);
  }

  @Bean
  public Topology processOptionsContractData(StreamsBuilder streamsBuilder) {
    ObjectMapper mapper = new ObjectMapper();
    Serde<PositionDbo> positionDataSerde = new JsonSerde<>(PositionDbo.class, mapper);

    final Consumed<String, PositionDbo> consumed =
        Consumed.with(Serdes.String(), positionDataSerde);
    final KStream<String, PositionDbo> positions =
        streamsBuilder.stream(kafkaConstantsConfig.getOPTION_POSITIONS_TOPIC(), consumed);

    KTable<String, PositionDbo> sortByTradingClassAndLastTradeDate =
        positions
            .selectKey(
                (key, value) ->
                    value.getContractDBO().getLastTradeDate()
                        + " "
                        + value.getContractDBO().getTradingClass())
            .groupByKey()
            .aggregate(
                () -> PositionDbo.builder().build(),
                (key, newPos, aggregatedPos) ->
                    streamOptionsContractDataCombineService.combinePositions(newPos, aggregatedPos),
                Materialized.<String, PositionDbo, KeyValueStore<Bytes, byte[]>>as(
                        kafkaConstantsConfig.getPOSITIONS_AGGREGATE_TOPIC())
                    .withKeySerde(Serdes.String())
                    .withValueSerde(positionDataSerde));

    sortByTradingClassAndLastTradeDate.toStream().to(kafkaConstantsConfig.getPOSITION_TOPIC());
    return streamsBuilder.build();
  }

  @Bean
  public Topology processOptionsMarketDataForStrategy(StreamsBuilder streamsBuilder) {
    ObjectMapper mapper = new ObjectMapper();
    Serde<OptionMarketData> optionMarketDataSerde = new JsonSerde<>(OptionMarketData.class, mapper);
    Serde<OptionChainData> optionChainDataSerde =
        new JsonSerde<>(OptionChainData.class, new ObjectMapper());

    final Consumed<String, OptionMarketData> consumed =
        Consumed.with(Serdes.String(), optionMarketDataSerde);
    final KStream<String, OptionMarketData> options =
        streamsBuilder.stream(kafkaConstantsConfig.getOPTION_MARKET_DATA_TOPIC(), consumed);

    KTable<String, OptionChainData> sortByTradingClassAndLastTradeDate =
        options
            .selectKey(
                (key, value) -> {
                  List<String> keys =
                      Arrays.stream(key.split(AutoDayTradeConstants.DELIMITER)).toList();
                  if (keys.size() > 1) {
                    return keys.get(0) + AutoDayTradeConstants.DELIMITER + keys.get(1);
                  } else {
                    return key;
                  }
                })
            .groupByKey()
            .aggregate(
                () -> OptionChainData.builder().build(),
                (key, newOptionData, chainData) ->
                    streamOptionChainDataCreator.buildChain(newOptionData, chainData),
                Materialized.<String, OptionChainData, KeyValueStore<Bytes, byte[]>>as(
                        kafkaConstantsConfig.getOPTION_MARKET_DATA_AGGREGATE_TOPIC())
                    .withKeySerde(Serdes.String())
                    .withValueSerde(optionChainDataSerde));

    sortByTradingClassAndLastTradeDate
        .toStream()
        .to(kafkaConstantsConfig.getOPTION_CHAIN_DATA_TOPIC());
    return streamsBuilder.build();
  }
}
