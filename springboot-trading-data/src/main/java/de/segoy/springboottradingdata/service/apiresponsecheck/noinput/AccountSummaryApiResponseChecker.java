package de.segoy.springboottradingdata.service.apiresponsecheck.noinput;

import de.segoy.springboottradingdata.config.KafkaConstantsConfig;
import de.segoy.springboottradingdata.config.PropertiesConfig;
import de.segoy.springboottradingdata.kafkaconsumer.KafkaApiCallEndService;
import de.segoy.springboottradingdata.model.entity.AccountSummaryData;
import de.segoy.springboottradingdata.repository.IBKRDataTypeRepository;
import org.springframework.stereotype.Service;

@Service
public class AccountSummaryApiResponseChecker extends AbstractNoInputListApiResponseChecker<AccountSummaryData> {


    public AccountSummaryApiResponseChecker(IBKRDataTypeRepository<AccountSummaryData> repository,
                                            PropertiesConfig propertiesConfig,
                                            KafkaApiCallEndService kafkaApiCallEndService, KafkaConstantsConfig kafkaConstantsConfig) {
        super(repository, propertiesConfig.getACCOUNT_SUMMARY_ID(), kafkaApiCallEndService,
                kafkaConstantsConfig.getACCOUNT_SUMMARY_TOPIC());
    }
}