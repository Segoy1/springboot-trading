package de.segoy.springboottradingweb.websocket;

import de.segoy.springboottradingdata.config.KafkaConstantsConfig;
import de.segoy.springboottradingdata.model.data.IBKRDataType;
import de.segoy.springboottradingdata.model.data.ProfitAndLossData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PositionPnLKafkaConsumer {

    private final KafkaConstantsConfig kafkaConstantsConfig;
    private final SimpMessagingTemplate messagingTemplate;

    public PositionPnLKafkaConsumer(KafkaConstantsConfig kafkaConstantsConfig, SimpMessagingTemplate messagingTemplate) {
        this.kafkaConstantsConfig = kafkaConstantsConfig;
        this.messagingTemplate = messagingTemplate;
    }

    @KafkaListener(topics = "${spring.kafka.names.topic.singlePnL}")
    public void consumeMessage(IBKRDataType message){
        ProfitAndLossData pnl = (ProfitAndLossData) message;
      log.info("Message received: " + pnl.getId());
      messagingTemplate.convertAndSend("/topic/"+kafkaConstantsConfig.getSINGLE_PNL_TOPIC(), pnl);
    }
}
