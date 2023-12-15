package de.segoy.springboottradingdata.service.apiresponsecheck;

import de.segoy.springboottradingdata.config.PropertiesConfig;
import de.segoy.springboottradingdata.kafkaconsumer.KafkaApiCallEndService;
import de.segoy.springboottradingdata.kafkaconsumer.KafkaConsumerProvider;
import de.segoy.springboottradingdata.model.IBKRDataTypeEntity;
import de.segoy.springboottradingdata.model.PositionData;
import de.segoy.springboottradingdata.repository.IBKRDataTypeRepository;
import de.segoy.springboottradingdata.service.RepositoryRefreshService;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Service
public class PositionApiResponseCheckerDataApiResponseChecker implements NoInputListApiResponseChecker<PositionData> {

    private final RepositoryRefreshService repositoryRefreshService;
    private final IBKRDataTypeRepository<PositionData> repository;
    private final PropertiesConfig propertiesConfig;
    private final KafkaApiCallEndService kafkaApiCallEndService;

    public PositionApiResponseCheckerDataApiResponseChecker(RepositoryRefreshService repositoryRefreshService,
                                                            IBKRDataTypeRepository<PositionData> repository,
                                                            PropertiesConfig propertiesConfig,
                                                            KafkaApiCallEndService kafkaApiCallEndService) {
        this.repositoryRefreshService = repositoryRefreshService;
        this.repository = repository;
        this.propertiesConfig = propertiesConfig;
        this.kafkaApiCallEndService = kafkaApiCallEndService;
    }

    @Override
    public List<PositionData> checkForApiResponseAndUpdate() {
        kafkaApiCallEndService.waitForApiCallToFinish(propertiesConfig.getPOSITION_CALL_ID());
        repositoryRefreshService.clearCache(repository);

        List<PositionData> positions = new ArrayList<>();
        repository.findAll().forEach(positions::add);
        return positions;
    }
}
