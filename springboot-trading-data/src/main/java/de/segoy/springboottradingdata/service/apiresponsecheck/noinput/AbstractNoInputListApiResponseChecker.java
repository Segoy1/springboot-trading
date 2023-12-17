package de.segoy.springboottradingdata.service.apiresponsecheck.noinput;

import de.segoy.springboottradingdata.kafkaconsumer.KafkaApiCallEndService;
import de.segoy.springboottradingdata.model.entity.IBKRDataTypeEntity;
import de.segoy.springboottradingdata.repository.IBKRDataTypeRepository;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractNoInputListApiResponseChecker<T extends IBKRDataTypeEntity> implements NoInputListApiResponseChecker<T> {

    private final IBKRDataTypeRepository<T> repository;
    private final int id;
    private final KafkaApiCallEndService kafkaApiCallEndService;
    private final String topic;

    public AbstractNoInputListApiResponseChecker(IBKRDataTypeRepository<T> repository, int id,
                                                 KafkaApiCallEndService kafkaApiCallEndService, String topic) {
        this.topic = topic;
        this.repository = repository;
        this.id = id;
        this.kafkaApiCallEndService = kafkaApiCallEndService;
    }

    @Override
    public List<T> checkForApiResponseAndUpdate() {
        kafkaApiCallEndService.waitForApiCallToFinish(id, topic);

        List<T> tableValues = new ArrayList<>();
        repository.findAll().forEach(tableValues::add);
        return tableValues;
    }
}
