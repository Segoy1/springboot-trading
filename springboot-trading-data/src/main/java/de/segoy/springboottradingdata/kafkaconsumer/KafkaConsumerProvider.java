package de.segoy.springboottradingdata.kafkaconsumer;

import de.segoy.springboottradingdata.model.data.kafka.KafkaDataType;
import java.util.List;
import org.apache.kafka.clients.consumer.Consumer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumerProvider {

    private final ConsumerFactory<String, KafkaDataType> entityConsumerFactory;

    public KafkaConsumerProvider(@Qualifier("BackendConsumerFactory") ConsumerFactory<String,
            KafkaDataType> entityConsumerFactory) {
        this.entityConsumerFactory = entityConsumerFactory;
    }

    public Consumer<String, KafkaDataType> createConsumerWithSubscription(List<String> topic) {
        Consumer<String, KafkaDataType> consumer = entityConsumerFactory.createConsumer();
        consumer.subscribe(topic);
        return consumer;
    }
}
