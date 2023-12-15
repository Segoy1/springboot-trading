package de.segoy.springboottradingdata.service.apiresponsecheck;

import de.segoy.springboottradingdata.config.PropertiesConfig;
import de.segoy.springboottradingdata.kafkaconsumer.KafkaApiCallEndService;
import de.segoy.springboottradingdata.kafkaconsumer.KafkaConsumerProvider;
import de.segoy.springboottradingdata.model.IBKRDataTypeEntity;
import de.segoy.springboottradingdata.model.PositionData;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class PositionApiResponseCheckerDataApiResponseChecker implements NoInputListApiResponseChecker<PositionData> {

    private final KafkaConsumerProvider kafkaConsumerProvider;
    private final PropertiesConfig propertiesConfig;
    private final KafkaApiCallEndService kafkaApiCallEndService;

    public PositionApiResponseCheckerDataApiResponseChecker(KafkaConsumerProvider kafkaConsumerProvider, PropertiesConfig propertiesConfig, KafkaApiCallEndService kafkaApiCallEndService) {
        this.kafkaConsumerProvider = kafkaConsumerProvider;
        this.propertiesConfig = propertiesConfig;
        this.kafkaApiCallEndService = kafkaApiCallEndService;
    }

    @Override
    public List<PositionData> checkForApiResponseAndUpdate() {
        List<PositionData> responseList = new ArrayList<>();
        ConsumerRecords<String, IBKRDataTypeEntity> records;
        Consumer<String, IBKRDataTypeEntity> consumer =
                kafkaConsumerProvider.createConsumerWithSubscription(List.of(propertiesConfig.getPOSITION_TOPIC()));
        kafkaApiCallEndService.waitForApiCallToFinish(propertiesConfig.getPositionsCallId());
        do {
            records = consumer.poll(Duration.ofMillis(50L));
            records.forEach((record) -> {
                responseList.add((PositionData) record.value());
            });
        } while (!records.isEmpty());
        consumer.close();
        return responseList;
    }
}
