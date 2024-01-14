package de.segoy.springboottradingdata.service.apiresponsecheck;

import de.segoy.springboottradingdata.config.KafkaConstantsConfig;
import de.segoy.springboottradingdata.kafkaconsumer.KafkaApiCallEndService;
import de.segoy.springboottradingdata.model.entity.ProfitAndLossData;
import de.segoy.springboottradingdata.repository.IBKRDataTypeRepository;
import de.segoy.springboottradingdata.service.RepositoryRefreshService;
import org.springframework.stereotype.Service;

@Service
public class AccountPnLApiResponseChecker extends AbstractApiResponseChecker<ProfitAndLossData> {

    public AccountPnLApiResponseChecker(IBKRDataTypeRepository<ProfitAndLossData> repository,
                                      RepositoryRefreshService repositoryRefreshService,
                                      KafkaApiCallEndService kafkaApiCallEndService,
                                      KafkaConstantsConfig kafkaConstantsConfig) {
            super(repository,repositoryRefreshService, kafkaApiCallEndService, kafkaConstantsConfig.getACCOUNT_PNL());
        }
}
