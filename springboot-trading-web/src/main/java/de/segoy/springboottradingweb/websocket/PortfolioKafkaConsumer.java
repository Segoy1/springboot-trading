package de.segoy.springboottradingweb.websocket;

import de.segoy.springboottradingdata.config.KafkaConstantsConfig;
import de.segoy.springboottradingdata.model.data.IBKRDataType;
import de.segoy.springboottradingdata.model.data.entity.PositionData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PortfolioKafkaConsumer {

    private final KafkaConstantsConfig kafkaConstantsConfig;
    private final SimpMessagingTemplate messagingTemplate;

    public PortfolioKafkaConsumer(KafkaConstantsConfig kafkaConstantsConfig, SimpMessagingTemplate messagingTemplate) {
        this.kafkaConstantsConfig = kafkaConstantsConfig;
        this.messagingTemplate = messagingTemplate;
    }

    @KafkaListener(topics = "${kafka.names.topic.positions}")
    public void consumeMessage(IBKRDataType message){
        PositionData position = (PositionData) message;
        log.info("Message received: " + position.getId());
        messagingTemplate.convertAndSend("/topic/"+kafkaConstantsConfig.getPOSITION_TOPIC(), position);
    }
}
