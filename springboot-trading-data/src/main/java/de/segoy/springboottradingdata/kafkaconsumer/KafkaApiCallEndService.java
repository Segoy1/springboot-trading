package de.segoy.springboottradingdata.kafkaconsumer;

import de.segoy.springboottradingdata.config.PropertiesConfig;
import de.segoy.springboottradingdata.model.IBKRDataTypeEntity;
import de.segoy.springboottradingdata.model.message.BaseMessage;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class KafkaApiCallEndService {

    private final KafkaConsumerProvider kafkaConsumerProvider;
    private final PropertiesConfig propertiesConfig;

    public KafkaApiCallEndService(KafkaConsumerProvider kafkaConsumerProvider, PropertiesConfig propertiesConfig) {
        this.kafkaConsumerProvider = kafkaConsumerProvider;
        this.propertiesConfig = propertiesConfig;
    }

    public void waitForApiCallToFinish(int id) {
        Consumer<String, IBKRDataTypeEntity> messageConsumer =
                kafkaConsumerProvider.createConsumerWithSubscription(List.of(propertiesConfig.getTWS_MESSAGE_TOPIC(),
                        propertiesConfig.getERROR_MESSAGE_TOPIC()));
        AtomicBoolean isDone = new AtomicBoolean(false);
        do {
            ConsumerRecords<String, IBKRDataTypeEntity> records = messageConsumer.poll(Duration.ofMillis(50L));
            records.forEach((record) -> {
                BaseMessage message = (BaseMessage) record.value();
                if(message.getMessageId().equals(id)){
                    isDone.set(true);
                }
            });
        } while (!isDone.get());
        messageConsumer.close();
    }
}
