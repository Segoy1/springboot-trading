package de.segoy.springboottradingdata.config;

import de.segoy.springboottradingdata.config.PropertiesConfig;
import de.segoy.springboottradingdata.model.entity.IBKRDataTypeEntity;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
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

    private final PropertiesConfig propertiesConfig;

    public KafkaProducerConfig(PropertiesConfig propertiesConfig) {
        this.propertiesConfig = propertiesConfig;
    }


    @Bean
    public ProducerFactory<String, String> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, propertiesConfig.getBOOTSTRAP_SERVERS());
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
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, propertiesConfig.getBOOTSTRAP_SERVERS());
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
    public NewTopic positionsTopic() {
        return TopicBuilder.name(propertiesConfig.getPOSITION_TOPIC()).build();
    }
    @Bean
    public NewTopic errorMessageTopic() {
        return TopicBuilder.name(propertiesConfig.getERROR_MESSAGE_TOPIC()).build();
    }
    @Bean
    public NewTopic twsMessageTopic() {
        return TopicBuilder.name(propertiesConfig.getTWS_MESSAGE_TOPIC()).build();
    }
    @Bean
    public NewTopic accountSummaryTopic() {
        return TopicBuilder.name(propertiesConfig.getACCOUNT_SUMMARY_TOPIC()).build();
    }
}
