package de.segoy.springboottradingdata.kafkaconsumer;

import de.segoy.springboottradingdata.config.KafkaConstantsConfig;
import de.segoy.springboottradingdata.model.entity.IBKRDataTypeEntity;
import de.segoy.springboottradingdata.model.entity.message.BaseMessage;
import de.segoy.springboottradingdata.model.entity.message.ErrorMessage;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;

@Component
public class KafkaApiCallEndService {

    private final KafkaConsumerProvider kafkaConsumerProvider;
    private final KafkaConstantsConfig kafkaConstantsConfig;

    public KafkaApiCallEndService(KafkaConsumerProvider kafkaConsumerProvider, KafkaConstantsConfig kafkaConstantsConfig) {
        this.kafkaConsumerProvider = kafkaConsumerProvider;
        this.kafkaConstantsConfig = kafkaConstantsConfig;
    }

    public void waitForApiCallToFinish(int id, String topic) {
        Consumer<String, IBKRDataTypeEntity> messageConsumer =
                kafkaConsumerProvider.createConsumerWithSubscription(List.of(topic,
                        kafkaConstantsConfig.getERROR_MESSAGE_TOPIC()));

        while (!processMessagesForId(id, messageConsumer)) {
            continue;
        }
        messageConsumer.close();
    }

    private boolean processMessagesForId(int id, Consumer<String, IBKRDataTypeEntity> messageConsumer) {
        ConsumerRecords<String, IBKRDataTypeEntity> records = messageConsumer.poll(Duration.ofMillis(50L));
        boolean processed = false;

        for (ConsumerRecord<String, IBKRDataTypeEntity> record : records) {
            BaseMessage message = (BaseMessage) record.value();
            if (message.getMessageId().equals(id)) {
                if (message.getClass().equals(ErrorMessage.class)) {
                    messageConsumer.close();
                    throw new RuntimeException("Error occured: " + message.getMessage());
                }
                processed = true;
            }
        }
        return processed;
    }
}
