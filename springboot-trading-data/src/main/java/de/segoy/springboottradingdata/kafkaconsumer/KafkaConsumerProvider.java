package de.segoy.springboottradingdata.kafkaconsumer;

import de.segoy.springboottradingdata.model.data.IBKRDataType;
import org.apache.kafka.clients.consumer.Consumer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class KafkaConsumerProvider {

    private final ConsumerFactory<String, IBKRDataType> entityConsumerFactory;

    public KafkaConsumerProvider(@Qualifier("BackendConsumerFactory") ConsumerFactory<String,
            IBKRDataType> entityConsumerFactory) {
        this.entityConsumerFactory = entityConsumerFactory;
    }

    public Consumer<String, IBKRDataType> createConsumerWithSubscription(List<String> topic) {
        Consumer<String, IBKRDataType> consumer = entityConsumerFactory.createConsumer();
        consumer.subscribe(topic);
        return consumer;
    }
}
