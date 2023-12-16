package de.segoy.springboottradingdata.service.messagehandler;

import de.segoy.springboottradingdata.config.PropertiesConfig;
import de.segoy.springboottradingdata.model.entity.IBKRDataTypeEntity;
import de.segoy.springboottradingdata.model.entity.message.TwsMessage;
import de.segoy.springboottradingdata.repository.message.TwsMessageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TwsMessageHandler {

    private final TwsMessageRepository twsMessageRepository;
    private final KafkaTemplate<String, IBKRDataTypeEntity> kafkaEntityTemplate;
    private final PropertiesConfig propertiesConfig;

    public TwsMessageHandler(TwsMessageRepository twsMessageRepository, KafkaTemplate<String, IBKRDataTypeEntity> kafkaEntityTemplate, PropertiesConfig propertiesConfig) {
        this.twsMessageRepository = twsMessageRepository;
        this.kafkaEntityTemplate = kafkaEntityTemplate;
        this.propertiesConfig = propertiesConfig;
    }

    public void handleMessage(int id, String message) {
        log.info(message);
        TwsMessage twsMessage = twsMessageRepository.save(TwsMessage.builder().messageId(id).message(message).build());
        kafkaEntityTemplate.send(propertiesConfig.getTWS_MESSAGE_TOPIC(), twsMessage);
    }
}
