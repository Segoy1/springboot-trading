package de.segoy.springboottradingweb.websocket;

import de.segoy.springboottradingdata.config.KafkaConstantsConfig;
import de.segoy.springboottradingdata.model.data.AccountSummaryData;
import de.segoy.springboottradingdata.model.data.OptionMarketData;
import de.segoy.springboottradingdata.model.data.ProfitAndLossData;
import de.segoy.springboottradingdata.model.data.StandardMarketData;
import de.segoy.springboottradingdata.model.data.entity.OrderData;
import de.segoy.springboottradingdata.model.data.entity.PositionData;
import de.segoy.springboottradingdata.model.data.message.ErrorMessage;
import de.segoy.springboottradingdata.service.ErrorCodeMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ApiResponseKafkaHandler {

    private final KafkaConstantsConfig kafkaConstantsConfig;
    private final SimpMessagingTemplate messagingTemplate;
    private final ErrorCodeMapper errorCodeMapper;

    @KafkaListener(groupId = "${kafka.consumer.group.id}", topics = "${kafka.names.topic.errorMessage}")
    public void consumeOptionMarketDataMessage(ErrorMessage errorMessage) {
        String topic = kafkaConstantsConfig.getERROR_MESSAGE_TOPIC();
        errorCodeMapper.mapError(errorMessage).ifPresent(handledErrorMessage -> {
            messagingTemplate.convertAndSend("/topic/" + topic, handledErrorMessage);
        });
    }
    @KafkaListener(groupId = "${kafka.consumer.group.id}", topics = "${kafka.names.topic.singlePnL}")
    public void consumeMessage(ProfitAndLossData message) {
        log.info("PNL Message received: " + message.getId());
        messagingTemplate.convertAndSend("/topic/" + kafkaConstantsConfig.getSINGLE_PNL_TOPIC(), message);
    }

    @KafkaListener(groupId = "${kafka.consumer.group.id}", topics = "${kafka.names.topic.accountPnL}")
    public void consumePnLMessage(ProfitAndLossData message) {
        String topic = kafkaConstantsConfig.getACCOUNT_PNL_TOPIC();
        log.info("Message received: " + topic + "-" + message.getId());
        messagingTemplate.convertAndSend("/topic/" + topic, message);
    }

    @KafkaListener(groupId = "${kafka.consumer.group.id}", topics = "${kafka.names.topic.accountSummary}")
    public void consumeSummaryMessage(AccountSummaryData message) {
        String summaryTopic = kafkaConstantsConfig.getACCOUNT_SUMMARY_TOPIC();
        log.info("Message received: " + summaryTopic + "-" + message.getTag());
        messagingTemplate.convertAndSend("/topic/" + summaryTopic, message);
    }

    @KafkaListener(groupId = "${kafka.consumer.group.id}", topics = "${kafka.names.topic.optionMarketData}")
    public void consumeOptionMarketDataMessage(OptionMarketData message) {
        String topic = kafkaConstantsConfig.getOPTION_MARKET_DATA_TOPIC();
        log.info("Tick: " + message.getTickerId() + message.getField());
        messagingTemplate.convertAndSend("/topic/" + topic, message);
    }

    @KafkaListener(groupId = "${kafka.consumer.group.id}", topics = "${kafka.names.topic.standardMarketData}")
    public void consumeSummaryMessage(StandardMarketData message) {
        String standardMarketTopic = kafkaConstantsConfig.getSTANDARD_MARKET_DATA_TOPIC();
        log.info("Tick: " + message.getTickerId() + message.getField());
        messagingTemplate.convertAndSend("/topic/" + standardMarketTopic, message);
    }

    @KafkaListener(groupId = "${kafka.consumer.group.id}", topics = "${kafka.names.topic.orderData}")
    public void consumeMessage(OrderData message) {
        log.info("Order received: " + message.getId());
        messagingTemplate.convertAndSend("/topic/" + kafkaConstantsConfig.getORDER_TOPIC(), message);
    }

    @KafkaListener(groupId = "${kafka.consumer.group.id}", topics = "${kafka.names.topic.positions}")
    public void consumeMessage(PositionData message) {
        log.info("Message received: " + message.getId());
        messagingTemplate.convertAndSend("/topic/" + kafkaConstantsConfig.getPOSITION_TOPIC(), message);
    }
}
