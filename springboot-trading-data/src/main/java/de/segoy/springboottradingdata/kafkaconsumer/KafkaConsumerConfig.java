package de.segoy.springboottradingdata.kafkaconsumer;

import de.segoy.springboottradingdata.config.KafkaConstantsConfig;
import de.segoy.springboottradingdata.model.entity.IBKRDataTypeEntity;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerConfig {

private final KafkaConstantsConfig kafkaConstantsConfig;

    public KafkaConsumerConfig(KafkaConstantsConfig kafkaConstantsConfig) {
        this.kafkaConstantsConfig = kafkaConstantsConfig;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, IBKRDataTypeEntity> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, IBKRDataTypeEntity> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(entityConsumerFactory());
        return factory;
    }

    @Bean ConsumerFactory<String, IBKRDataTypeEntity> entityConsumerFactory(){
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConstantsConfig.getBOOTSTRAP_SERVERS());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaConstantsConfig.getGroupId());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        props.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, StringDeserializer.class);
        props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class.getName());
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, "de.segoy.springboottradingdata.model.entity.IBKRDataTypeEntity");
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "de.segoy.springboottradingdata.model.*");

        return new DefaultKafkaConsumerFactory<>(props);
    }
}
