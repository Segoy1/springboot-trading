package de.segoy.springboottradingdata.service.apiresponsecheck.noinput;

import de.segoy.springboottradingdata.config.PropertiesConfig;
import de.segoy.springboottradingdata.kafkaconsumer.KafkaApiCallEndService;
import de.segoy.springboottradingdata.model.entity.PositionData;
import de.segoy.springboottradingdata.repository.IBKRDataTypeRepository;
import de.segoy.springboottradingdata.service.RepositoryRefreshService;
import org.springframework.stereotype.Service;

@Service
public class PositionApiResponseChecker extends AbstractNoInputListApiResponseChecker<PositionData> {


    public PositionApiResponseChecker(RepositoryRefreshService repositoryRefreshService,
                                      IBKRDataTypeRepository<PositionData> repository,
                                      PropertiesConfig propertiesConfig,
                                      KafkaApiCallEndService kafkaApiCallEndService) {
        super(repositoryRefreshService, repository, propertiesConfig.getPOSITION_CALL_ID(), kafkaApiCallEndService);
    }
}
