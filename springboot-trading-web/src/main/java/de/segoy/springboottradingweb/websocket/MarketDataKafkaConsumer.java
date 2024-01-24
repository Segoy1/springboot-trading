package de.segoy.springboottradingweb.websocket;

import de.segoy.springboottradingdata.config.KafkaConstantsConfig;
import de.segoy.springboottradingdata.model.data.IBKRDataType;
import de.segoy.springboottradingdata.model.data.OptionMarketData;
import de.segoy.springboottradingdata.model.data.StandardMarketData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MarketDataKafkaConsumer {
    private final KafkaConstantsConfig kafkaConstantsConfig;
    private final SimpMessagingTemplate messagingTemplate;

    public MarketDataKafkaConsumer(KafkaConstantsConfig kafkaConstantsConfig, SimpMessagingTemplate messagingTemplate) {
        this.kafkaConstantsConfig = kafkaConstantsConfig;
        this.messagingTemplate = messagingTemplate;
    }

    @KafkaListener(topics = "${spring.kafka.names.topic.optionMarketData}")
    public void consumeOptionMarketDataMessage(IBKRDataType message){
        OptionMarketData optionMarketData = (OptionMarketData) message;
        String topic = kafkaConstantsConfig.getOPTION_MARKET_DATA_TOPIC();
        log.info("Tick: "+ optionMarketData.getTickerId()+ optionMarketData.getField());
        messagingTemplate.convertAndSend("/topic/"+ topic, message);
    }
    @KafkaListener(topics = "${spring.kafka.names.topic.standardMarketData}")
    public void consumeSummaryMessage(IBKRDataType message){
        StandardMarketData standardMarketData = (StandardMarketData) message;
        String standardMarketTopic = kafkaConstantsConfig.getSTANDARD_MARKET_DATA_TOPIC();
        log.info("Tick: "+ standardMarketData.getTickerId() + standardMarketData.getField());
        messagingTemplate.convertAndSend("/topic/"+ standardMarketTopic, standardMarketData);
    }
}
