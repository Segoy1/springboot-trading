package de.segoy.springboottradingdata.service.apiresponsecheck;

import de.segoy.springboottradingdata.kafkaconsumer.KafkaApiCallEndService;
import de.segoy.springboottradingdata.model.OrderData;
import de.segoy.springboottradingdata.repository.IBKRDataTypeRepository;
import de.segoy.springboottradingdata.service.RepositoryRefreshService;
import org.springframework.stereotype.Service;

@Service
class OrderDataApiResponseChecker extends AbstractApiResponseChecker<OrderData> {

    public OrderDataApiResponseChecker(IBKRDataTypeRepository<OrderData> repository,
                                       RepositoryRefreshService repositoryRefreshService,
                                       KafkaApiCallEndService kafkaApiCallEndService) {
        super(repository, repositoryRefreshService, kafkaApiCallEndService);
    }
}
