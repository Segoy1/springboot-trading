package de.segoy.springboottradingdata.service.apiresponsecheck;

import de.segoy.springboottradingdata.kafkaconsumer.KafkaApiCallEndService;
import de.segoy.springboottradingdata.model.entity.IBKRDataTypeEntity;
import de.segoy.springboottradingdata.repository.IBKRDataTypeRepository;
import de.segoy.springboottradingdata.service.RepositoryRefreshService;

import java.util.Optional;

abstract class AbstractApiResponseChecker<T extends IBKRDataTypeEntity> implements OptionalApiResponseChecker<T> {

    private final IBKRDataTypeRepository<T> repository;
    private final RepositoryRefreshService repositoryRefreshService;
    private final KafkaApiCallEndService kafkaApiCallEndService;
    private final String topic;

    public AbstractApiResponseChecker(IBKRDataTypeRepository<T> repository,
                                      RepositoryRefreshService repositoryRefreshService,
                                      KafkaApiCallEndService kafkaApiCallEndService, String topic) {
        this.repositoryRefreshService = repositoryRefreshService;
        this.repository = repository;
        this.kafkaApiCallEndService = kafkaApiCallEndService;
        this.topic = topic;
    }

    public Optional<T> checkForApiResponseAndUpdate(int id) {
        kafkaApiCallEndService.waitForApiCallToFinish(id, topic);
        repositoryRefreshService.clearCache(repository);
        return repository.findById((long) id);
    }
}

