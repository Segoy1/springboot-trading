package de.segoy.springboottradingdata.service.apiresponsecheck.noinput;

import de.segoy.springboottradingdata.config.PropertiesConfig;
import de.segoy.springboottradingdata.kafkaconsumer.KafkaApiCallEndService;
import de.segoy.springboottradingdata.model.entity.OrderData;
import de.segoy.springboottradingdata.repository.IBKRDataTypeRepository;
import de.segoy.springboottradingdata.service.RepositoryRefreshService;
import org.springframework.stereotype.Service;

@Service
public class OpenOrdersApiResponseChecker extends AbstractNoInputListApiResponseChecker<OrderData> {


    public OpenOrdersApiResponseChecker(RepositoryRefreshService repositoryRefreshService,
                                        IBKRDataTypeRepository<OrderData> repository,
                                        PropertiesConfig propertiesConfig,
                                        KafkaApiCallEndService kafkaApiCallEndService) {
        super(repositoryRefreshService, repository, propertiesConfig.getOPEN_ORDERS_ID(), kafkaApiCallEndService);
    }
}