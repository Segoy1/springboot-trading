package de.segoy.springboottradingdata.service.apiresponsecheck;

import de.segoy.springboottradingdata.config.KafkaConstantsConfig;
import de.segoy.springboottradingdata.kafkaconsumer.KafkaApiCallEndService;
import de.segoy.springboottradingdata.model.entity.ContractData;
import de.segoy.springboottradingdata.repository.IBKRDataTypeRepository;
import de.segoy.springboottradingdata.service.RepositoryRefreshService;
import org.springframework.stereotype.Service;

@Service
class ContractApiResponseChecker extends AbstractApiResponseChecker<ContractData> {

    public ContractApiResponseChecker(IBKRDataTypeRepository<ContractData> repository,
                                      RepositoryRefreshService repositoryRefreshService,
                                      KafkaApiCallEndService kafkaApiCallEndService,
                                      KafkaConstantsConfig kafkaConstantsConfig) {
        super(repository,repositoryRefreshService, kafkaApiCallEndService, kafkaConstantsConfig.getCONTRACT_TOPIC());
    }
}
