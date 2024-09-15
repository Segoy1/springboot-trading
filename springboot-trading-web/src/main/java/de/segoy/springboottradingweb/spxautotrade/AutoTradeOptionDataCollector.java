package de.segoy.springboottradingweb.spxautotrade;

import de.segoy.springboottradingdata.model.data.OptionMarketData;
import de.segoy.springboottradingdata.optionstradingservice.LastTradeDateBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AutoTradeOptionDataCollector {

    private final LastTradeDateBuilder lastTradeDateBuilder;

    @KafkaListener(groupId = "${kafka.consumer.auto.group.id}", topics = "${kafka.names.topic.optionMarketData}")
    public void collectPutAndCallData(OptionMarketData message){
        if(message.getTickerId() == lastTradeDateBuilder.getDateIntFromToday()){
            log.info("Tick: " + message.getTickerId() + message.getField());

        }
    }
}
