package de.segoy.springboottradingdata.config;

import de.segoy.springboottradingdata.model.entity.IBKRDataTypeEntity;
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
public class KafkaProducerConfig {

    private final KafkaConstantsConfig kafkaConstantsConfig;

    public KafkaProducerConfig(KafkaConstantsConfig kafkaConstantsConfig) {
        this.kafkaConstantsConfig = kafkaConstantsConfig;
    }


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
    public ProducerFactory<String, IBKRDataTypeEntity> entityProducerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConstantsConfig.getBOOTSTRAP_SERVERS());
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<String, IBKRDataTypeEntity>(configProps, new StringSerializer(),
                new JsonSerializer<IBKRDataTypeEntity>());
    }

    @Bean
    public KafkaTemplate<String, IBKRDataTypeEntity> kafkaEntityTemplate() {
        return new KafkaTemplate<>(entityProducerFactory());
    }

    @Bean
    @Qualifier("${spring.kafka.names.topic.historicalData}")
    public NewTopic historicalTopic(){
        return TopicBuilder.name(kafkaConstantsConfig.getHISTORICAL_TOPIC()).build();
    }
    @Bean
    @Qualifier("${spring.kafka.names.topic.orderData}")
    public NewTopic orderTopicString (){
        return TopicBuilder.name(kafkaConstantsConfig.getORDER_TOPIC()).build();
    }
    @Bean
    @Qualifier("${spring.kafka.names.topic.contractData}")
    public NewTopic contractTopic(){
        return TopicBuilder.name(kafkaConstantsConfig.getCONTRACT_TOPIC()).build();
    }
    @Bean
    @Value("${spring.kafka.names.topic.errorMessage}")
    public NewTopic errorMessageTopic() {
        return TopicBuilder.name(kafkaConstantsConfig.getERROR_MESSAGE_TOPIC()).build();
    }
    @Bean
    @Qualifier("${spring.kafka.names.topic.positions}")
    public NewTopic positionsTopic() {
        return TopicBuilder.name(kafkaConstantsConfig.getPOSITION_TOPIC()).build();
    }
    @Bean
    @Qualifier("${spring.kafka.names.topic.openOrders}")
    public NewTopic openOrdersTopic() {
        return TopicBuilder.name(kafkaConstantsConfig.getOPEN_ORDER_TOPIC()).build();
    }
    @Bean
    @Qualifier("${spring.kafka.names.topic.accountSummary}")
    public NewTopic accountSummaryTopic() {
        return TopicBuilder.name(kafkaConstantsConfig.getACCOUNT_SUMMARY_TOPIC()).build();
    }
    @Bean
    @Qualifier("${spring.kafka.names.topic.accountPnL}")
    public NewTopic accountPnLTopic() {
        return TopicBuilder.name(kafkaConstantsConfig.getACCOUNT_PNL()).build();
    }

}
