package de.segoy.springboottradingdata.kafkaconsumer;

import de.segoy.springboottradingdata.config.PropertiesConfig;
import de.segoy.springboottradingdata.model.entity.IBKRDataTypeEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KafkaListenerServiceTest {

//    @KafkaListener(groupId = "marketDataGroup",topics = "errorMessage")
//    public void handleMessage(IBKRDataTypeEntity msg){
//      log.warn(msg.toString());
//    }
}
