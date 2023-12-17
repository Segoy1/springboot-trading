package de.segoy.springboottradingdata.service.apiresponsecheck.noinput;

import de.segoy.springboottradingdata.config.KafkaConstantsConfig;
import de.segoy.springboottradingdata.config.PropertiesConfig;
import de.segoy.springboottradingdata.kafkaconsumer.KafkaApiCallEndService;
import de.segoy.springboottradingdata.model.entity.OrderData;
import de.segoy.springboottradingdata.repository.IBKRDataTypeRepository;
import org.springframework.stereotype.Service;

@Service
public class OpenOrdersApiResponseChecker extends AbstractNoInputListApiResponseChecker<OrderData> {


    public OpenOrdersApiResponseChecker(IBKRDataTypeRepository<OrderData> repository,
                                        PropertiesConfig propertiesConfig,
                                        KafkaApiCallEndService kafkaApiCallEndService,
                                        KafkaConstantsConfig kafkaConstantsConfig) {
        super(repository, propertiesConfig.getOPEN_ORDERS_ID(), kafkaApiCallEndService,
                kafkaConstantsConfig.getOPEN_ORDER_TOPIC());
    }
}