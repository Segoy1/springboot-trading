package de.segoy.springboottradingdata.service.apiresponsecheck.noinput;

import de.segoy.springboottradingdata.config.KafkaConstantsConfig;
import de.segoy.springboottradingdata.config.PropertiesConfig;
import de.segoy.springboottradingdata.kafkaconsumer.KafkaApiCallEndService;
import de.segoy.springboottradingdata.model.entity.PositionData;
import de.segoy.springboottradingdata.repository.IBKRDataTypeRepository;
import org.springframework.stereotype.Service;

@Service
public class PositionApiResponseChecker extends AbstractNoInputListApiResponseChecker<PositionData> {


    public PositionApiResponseChecker(IBKRDataTypeRepository<PositionData> repository,
                                      PropertiesConfig propertiesConfig,
                                      KafkaApiCallEndService kafkaApiCallEndService,
                                      KafkaConstantsConfig kafkaConstantsConfig) {
        super(repository, propertiesConfig.getPOSITION_CALL_ID(), kafkaApiCallEndService,
                kafkaConstantsConfig.getPOSITION_TOPIC());
    }
}
