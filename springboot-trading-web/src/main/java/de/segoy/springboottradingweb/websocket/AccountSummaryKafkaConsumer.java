package de.segoy.springboottradingweb.websocket;

import de.segoy.springboottradingdata.config.KafkaConstantsConfig;
import de.segoy.springboottradingdata.model.entity.AccountSummaryData;
import de.segoy.springboottradingdata.model.entity.IBKRDataTypeEntity;
import de.segoy.springboottradingdata.model.entity.ProfitAndLossData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AccountSummaryKafkaConsumer {
    private final KafkaConstantsConfig kafkaConstantsConfig;
    private final SimpMessagingTemplate messagingTemplate;

    public AccountSummaryKafkaConsumer(KafkaConstantsConfig kafkaConstantsConfig, SimpMessagingTemplate messagingTemplate) {
        this.kafkaConstantsConfig = kafkaConstantsConfig;
        this.messagingTemplate = messagingTemplate;
    }

    @KafkaListener(topics = "${spring.kafka.names.topic.accountPnL}")
    public void consumePnLMessage(IBKRDataTypeEntity message){
        ProfitAndLossData pnl = (ProfitAndLossData) message;
        log.warn("Message received: " + pnl.getId());
        messagingTemplate.convertAndSend("/topic/"+kafkaConstantsConfig.getACCOUNT_PNL_TOPIC(), message);
    }
    @KafkaListener(topics = "${spring.kafka.names.topic.accountSummary}")
    public void consumeSummaryMessage(IBKRDataTypeEntity message){
        AccountSummaryData summary = (AccountSummaryData) message;
        log.warn("Message received: " + summary.getId());
        messagingTemplate.convertAndSend("/topic/"+kafkaConstantsConfig.getACCOUNT_SUMMARY_TOPIC(), summary);
    }
}
