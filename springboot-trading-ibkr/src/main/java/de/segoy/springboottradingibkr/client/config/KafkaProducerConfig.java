package de.segoy.springboottradingibkr.client.config;

import de.segoy.springboottradingdata.config.PropertiesConfig;
import de.segoy.springboottradingdata.model.IBKRDataTypeEntity;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
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
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, propertiesConfig.getBootstrapServers());
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
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, propertiesConfig.getBootstrapServers());
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
    public NewTopic contractDataTopic() {
        return TopicBuilder.name(propertiesConfig.getContractTopic()).build();
    }

    @Bean
    public NewTopic historicalData() {
        return TopicBuilder.name(propertiesConfig.getHistoricalTopic()).build();
    }
}
