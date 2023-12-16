package de.segoy.springboottradingdata.service.apiresponsecheck.noinput;

import de.segoy.springboottradingdata.config.PropertiesConfig;
import de.segoy.springboottradingdata.kafkaconsumer.KafkaApiCallEndService;
import de.segoy.springboottradingdata.model.entity.OrderData;
import de.segoy.springboottradingdata.repository.IBKRDataTypeRepository;
import de.segoy.springboottradingdata.service.RepositoryRefreshService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OpenOrdersApiResponseChecker implements NoInputListApiResponseChecker<OrderData> {

    private final RepositoryRefreshService repositoryRefreshService;
    private final IBKRDataTypeRepository<OrderData> repository;
    private final PropertiesConfig propertiesConfig;
    private final KafkaApiCallEndService kafkaApiCallEndService;

    public OpenOrdersApiResponseChecker(RepositoryRefreshService repositoryRefreshService,
                                      IBKRDataTypeRepository<OrderData> repository,
                                      PropertiesConfig propertiesConfig,
                                      KafkaApiCallEndService kafkaApiCallEndService) {
        this.repositoryRefreshService = repositoryRefreshService;
        this.repository = repository;
        this.propertiesConfig = propertiesConfig;
        this.kafkaApiCallEndService = kafkaApiCallEndService;
    }

    @Override
    public List<OrderData> checkForApiResponseAndUpdate() {
        kafkaApiCallEndService.waitForApiCallToFinish(propertiesConfig.getOPEN_ORDERS_ID());
        repositoryRefreshService.clearCache(repository);

        List<OrderData> orders = new ArrayList<>();
        repository.findAll().forEach(orders::add);
        return orders;
    }
}