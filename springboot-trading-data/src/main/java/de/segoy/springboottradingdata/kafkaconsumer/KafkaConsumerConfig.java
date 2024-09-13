package de.segoy.springboottradingdata.kafkaconsumer;

import de.segoy.springboottradingdata.config.KafkaConstantsConfig;
import de.segoy.springboottradingdata.model.data.IBKRDataType;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Qualifier;
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
@RequiredArgsConstructor
public class KafkaConsumerConfig {

    private final KafkaConstantsConfig kafkaConstantsConfig;


    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, IBKRDataType> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, IBKRDataType> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(websocketConsumerFactory());
        return factory;
    }

    @Bean
    @Qualifier("WebsocketConsumerFactory")
    ConsumerFactory<String, IBKRDataType> websocketConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(getProps());
    }

    @Bean
    @Qualifier("BackendConsumerFactory")
    ConsumerFactory<String, IBKRDataType> backendResponseConsumerFactory() {
        Map<String, Object> props = getProps();
        props.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaConstantsConfig.getRestResponseGroupId());
        return new DefaultKafkaConsumerFactory<>(props);
    }

    private Map<String, Object> getProps() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConstantsConfig.getBOOTSTRAP_SERVERS());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        props.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, StringDeserializer.class);
        props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class.getName());
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "de.segoy.springboottradingdata.model.*");

        return props;
    }
}
