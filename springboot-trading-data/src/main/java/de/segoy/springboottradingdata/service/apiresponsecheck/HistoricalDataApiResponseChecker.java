package de.segoy.springboottradingdata.service.apiresponsecheck;

import de.segoy.springboottradingdata.config.PropertiesConfig;
import de.segoy.springboottradingdata.model.HistoricalData;
import de.segoy.springboottradingdata.model.IBKRDataTypeEntity;
import de.segoy.springboottradingdata.repository.HistoricalDataRepository;
import de.segoy.springboottradingdata.service.RepositoryRefreshService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
class HistoricalDataApiResponseChecker implements ApiResponseCheckerForList<HistoricalData> {

    private final HistoricalDataRepository repository;
    private final RepositoryRefreshService repositoryRefreshService;
    private final KafkaConsumer<String, IBKRDataTypeEntity> kafkaConsumer;

    public HistoricalDataApiResponseChecker(HistoricalDataRepository repository, RepositoryRefreshService repositoryRefreshService, KafkaConsumer<String, IBKRDataTypeEntity> kafkaConsumer) {
        this.repositoryRefreshService = repositoryRefreshService;
        this.repository = repository;
        this.kafkaConsumer = kafkaConsumer;
    }


    @Override
    public List<HistoricalData> checkForApiResponseAndUpdate(int id) {
        List<HistoricalData> responseList = new ArrayList<>();
        ConsumerRecords<String, IBKRDataTypeEntity> records;
        do {
            repositoryRefreshService.clearCacheAndWait(repository);
            records = kafkaConsumer.poll(Duration.ofMillis(10L));
            records.forEach((record) -> {
                responseList.add((HistoricalData) record.value());
            });
        } while (!records.isEmpty() && !responseList.isEmpty());

        return responseList;
    }
}
