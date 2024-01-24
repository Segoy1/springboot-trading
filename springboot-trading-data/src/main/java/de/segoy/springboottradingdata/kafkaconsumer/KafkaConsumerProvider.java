package de.segoy.springboottradingdata.kafkaconsumer;

import de.segoy.springboottradingdata.model.entity.IBKRDataTypeEntity;
import org.apache.kafka.clients.consumer.Consumer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class KafkaConsumerProvider {

    private final ConsumerFactory<String, IBKRDataTypeEntity> entityConsumerFactory;

    public KafkaConsumerProvider(@Qualifier("RestResponseConsumerFactory") ConsumerFactory<String,
            IBKRDataTypeEntity> entityConsumerFactory) {
        this.entityConsumerFactory = entityConsumerFactory;
    }

    public Consumer<String, IBKRDataTypeEntity> createConsumerWithSubscription(List<String> topic) {
        Consumer<String, IBKRDataTypeEntity> consumer = entityConsumerFactory.createConsumer();
        consumer.subscribe(topic);
        return consumer;
    }
}
