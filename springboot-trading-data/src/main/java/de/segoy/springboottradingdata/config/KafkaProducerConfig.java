package de.segoy.springboottradingdata.config;

import de.segoy.springboottradingdata.model.data.IBKRDataType;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class KafkaProducerConfig {

    private final KafkaConstantsConfig kafkaConstantsConfig;

    @Bean
    public ProducerFactory<String, String> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConstantsConfig.getBOOTSTRAP_SERVERS());
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public ProducerFactory<String, IBKRDataType> entityProducerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConstantsConfig.getBOOTSTRAP_SERVERS());
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, IBKRDataType> kafkaEntityTemplate() {
        return new KafkaTemplate<>(entityProducerFactory());
    }

    @Bean
    @Qualifier("${kafka.names.topic.historicalData}")
    public NewTopic historicalTopic() {
        return topicHelper(kafkaConstantsConfig.getHISTORICAL_TOPIC());
    }

    @Bean
    @Qualifier("${kafka.names.topic.orderData}")
    public NewTopic orderTopicString() {
        return topicHelper(kafkaConstantsConfig.getORDER_TOPIC());
    }

    @Bean
    @Qualifier("${kafka.names.topic.contractData}")
    public NewTopic contractTopic() {
        return topicHelper(kafkaConstantsConfig.getCONTRACT_TOPIC());
    }

    @Bean
    @Value("${kafka.names.topic.errorMessage}")
    public NewTopic errorMessageTopic() {
        return topicHelper(kafkaConstantsConfig.getERROR_MESSAGE_TOPIC());
    }

    @Bean
    @Qualifier("${kafka.names.topic.positions}")
    public NewTopic positionsTopic() {
        return topicHelper(kafkaConstantsConfig.getPOSITION_TOPIC());
    }

    @Bean
    @Qualifier("${kafka.names.topic.openOrders}")
    public NewTopic openOrdersTopic() {
        return topicHelper(kafkaConstantsConfig.getOPEN_ORDER_TOPIC());
    }

    @Bean
    @Qualifier("${kafka.names.topic.accountSummary}")
    public NewTopic accountSummaryTopic() {
        return topicHelper(kafkaConstantsConfig.getACCOUNT_SUMMARY_TOPIC());
    }

    @Bean
    @Qualifier("${kafka.names.topic.accountPnL}")
    public NewTopic accountPnLTopic() {
        return topicHelper(kafkaConstantsConfig.getACCOUNT_PNL_TOPIC());
    }

    @Bean
    @Qualifier("${kafka.names.topic.singlePnL}")
    public NewTopic singlePnLTopic() {
        return compactTopicHelper(kafkaConstantsConfig.getSINGLE_PNL_TOPIC());
    }

    @Bean
    @Qualifier("${kafka.names.topic.optionMarketData}")
    public NewTopic optionMarketDataTopic() {
        return compactTopicHelper(kafkaConstantsConfig.getOPTION_MARKET_DATA_TOPIC());
    }

    @Bean
    @Qualifier("${kafka.names.topic.standardMarketData}")
    public NewTopic standardMarketData() {
        return compactTopicHelper(kafkaConstantsConfig.getSTANDARD_MARKET_DATA_TOPIC());
    }
    @Bean
    @Qualifier("${kafka.names.topic.streams.optionPositions}")
    public NewTopic optionsPositionsTopic(){
        return topicHelper(kafkaConstantsConfig.getOPTION_POSITIONS_TOPIC());
    }
    @Bean
    @Qualifier("${kafka.names.topic.streams.aggregatePositions}")
    public NewTopic positionsAggregateTopic(){
        return TopicBuilder.name(kafkaConstantsConfig.getPOSITIONS_AGGREGATE_TOPIC()).compact().partitions(2).build();
    }

    private NewTopic topicHelper(String topicName) {
        return TopicBuilder.name(topicName).build();
    }
    private NewTopic compactTopicHelper(String topicName){
        return TopicBuilder.name(topicName).compact().build();
    }
}
