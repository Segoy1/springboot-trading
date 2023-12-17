package de.segoy.springboottradingdata.service.apiresponsecheck.noinput;

import de.segoy.springboottradingdata.config.PropertiesConfig;
import de.segoy.springboottradingdata.kafkaconsumer.KafkaApiCallEndService;
import de.segoy.springboottradingdata.model.entity.AccountSummaryData;
import de.segoy.springboottradingdata.repository.IBKRDataTypeRepository;
import de.segoy.springboottradingdata.service.RepositoryRefreshService;
import org.springframework.stereotype.Service;

@Service
public class AccountSummaryApiResponseChecker extends AbstractNoInputListApiResponseChecker<AccountSummaryData> {


    public AccountSummaryApiResponseChecker(RepositoryRefreshService repositoryRefreshService,
                                            IBKRDataTypeRepository<AccountSummaryData> repository,
                                            PropertiesConfig propertiesConfig,
                                            KafkaApiCallEndService kafkaApiCallEndService) {
        super(repositoryRefreshService, repository, propertiesConfig.getACCOUNT_SUMMARY_ID(), kafkaApiCallEndService);
    }
}