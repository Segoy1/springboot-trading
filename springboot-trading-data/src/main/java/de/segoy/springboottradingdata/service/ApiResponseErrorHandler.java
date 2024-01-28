package de.segoy.springboottradingdata.service;

import de.segoy.springboottradingdata.config.KafkaConstantsConfig;
import de.segoy.springboottradingdata.kafkaconsumer.KafkaConsumerProvider;
import de.segoy.springboottradingdata.model.data.IBKRDataType;
import de.segoy.springboottradingdata.model.data.message.ErrorMessage;
import jakarta.annotation.PreDestroy;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Service
public class ApiResponseErrorHandler {


    private final ErrorCodeMapper errorCodeMapper;
    private final Consumer<String, IBKRDataType> consumer;

    public ApiResponseErrorHandler(KafkaConsumerProvider kafkaConsumerProvider,
                                   KafkaConstantsConfig kafkaConstantsConfig,
                                   ErrorCodeMapper errorCodeMapper) {
        this.errorCodeMapper = errorCodeMapper;
        this.consumer = kafkaConsumerProvider.createConsumerWithSubscription(
                List.of(kafkaConstantsConfig.getERROR_MESSAGE_TOPIC()));

    }

    public boolean isErrorForId(int id) {

        ConsumerRecords<String, IBKRDataType> records = consumer.poll(Duration.ofMillis(100L));
        for (ConsumerRecord<String, IBKRDataType> record : records) {
            if (record.key().equals(Integer.toString(id))) {
                errorCodeMapper.mapError((ErrorMessage) record.value());
                return true;
            }
        }
        return false;
    }
    @PreDestroy
    private void closeConsumer(){
        this.consumer.close();
    }
}
