package de.segoy.springboottradingdata.kafkaconsumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KafkaListenerServiceTest {

//    @KafkaListener(groupId = "marketDataGroup",topics = "contracts")
//    public void handleMessage(String msg){
//      log.warn(msg);
//    }
}
