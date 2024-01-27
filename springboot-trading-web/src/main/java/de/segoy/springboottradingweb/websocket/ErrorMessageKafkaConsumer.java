package de.segoy.springboottradingweb.websocket;

import de.segoy.springboottradingdata.config.KafkaConstantsConfig;
import de.segoy.springboottradingdata.model.data.IBKRDataType;
import de.segoy.springboottradingdata.model.data.message.ErrorMessage;
import de.segoy.springboottradingdata.service.ErrorCodeMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class ErrorMessageKafkaConsumer {
    private final KafkaConstantsConfig kafkaConstantsConfig;
    private final SimpMessagingTemplate messagingTemplate;
    private final ErrorCodeMapper errorCodeMapper;

    public ErrorMessageKafkaConsumer(KafkaConstantsConfig kafkaConstantsConfig, SimpMessagingTemplate messagingTemplate, ErrorCodeMapper errorCodeMapper) {
        this.kafkaConstantsConfig = kafkaConstantsConfig;
        this.messagingTemplate = messagingTemplate;
        this.errorCodeMapper = errorCodeMapper;
    }

    @KafkaListener(topics = "${spring.kafka.names.topic.errorMessage}")
    public void consumeOptionMarketDataMessage(IBKRDataType message){
        ErrorMessage errorMessage = (ErrorMessage) message;
        String topic = kafkaConstantsConfig.getERROR_MESSAGE_TOPIC();
        errorCodeMapper.mapError(errorMessage);
        messagingTemplate.convertAndSend("/topic/"+ topic, message);
    }
}
