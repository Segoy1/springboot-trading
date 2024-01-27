package de.segoy.springboottradingdata.service;

import de.segoy.springboottradingdata.config.KafkaConstantsConfig;
import de.segoy.springboottradingdata.model.data.IBKRDataType;
import de.segoy.springboottradingdata.model.data.message.ErrorMessage;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Service
public class ApiResponseErrorHandler {

    private final ConsumerFactory<String, IBKRDataType> kafkaFactory;
    private final KafkaConstantsConfig kafkaConstantsConfig;
    private final ErrorCodeMapper errorCodeMapper;

    public ApiResponseErrorHandler(@Qualifier("BackendConsumerFactory") ConsumerFactory<String, IBKRDataType> kafkaFactory, KafkaConstantsConfig kafkaConstantsConfig, ErrorCodeMapper errorCodeMapper) {
        this.kafkaFactory = kafkaFactory;
        this.kafkaConstantsConfig = kafkaConstantsConfig;
        this.errorCodeMapper = errorCodeMapper;
    }

    public boolean isErrorForId(int id) {
        Consumer<String, IBKRDataType> consumer = kafkaFactory.createConsumer();
        consumer.subscribe(List.of(kafkaConstantsConfig.getERROR_MESSAGE_TOPIC()));

        ConsumerRecords<String, IBKRDataType> records = consumer.poll(Duration.ofMillis(100L));
        for (ConsumerRecord<String, IBKRDataType> record : records) {
            if (record.key().equals(Integer.toString(id))) {
                errorCodeMapper.mapError((ErrorMessage) record.value());
                return true;
            }

        }
        return false;
    }
}
