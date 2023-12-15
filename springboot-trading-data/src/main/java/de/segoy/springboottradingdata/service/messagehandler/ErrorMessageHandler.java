package de.segoy.springboottradingdata.service.messagehandler;

import de.segoy.springboottradingdata.config.PropertiesConfig;
import de.segoy.springboottradingdata.model.IBKRDataTypeEntity;
import de.segoy.springboottradingdata.model.message.ErrorMessage;
import de.segoy.springboottradingdata.repository.message.ErrorMessageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ErrorMessageHandler {

    private final ErrorMessageRepository errorMessageRepository;
    private final KafkaTemplate<String, IBKRDataTypeEntity> kafkaEntityTemplate;
    private final PropertiesConfig propertiesConfig;

    public ErrorMessageHandler(ErrorMessageRepository errorMessageRepository, KafkaTemplate<String, IBKRDataTypeEntity> kafkaEntityTemplate, PropertiesConfig propertiesConfig) {
        this.errorMessageRepository = errorMessageRepository;
        this.kafkaEntityTemplate = kafkaEntityTemplate;
        this.propertiesConfig = propertiesConfig;
    }

    public void handleError(int id, String message) {
        log.warn(message);
        ErrorMessage error = errorMessageRepository.save(ErrorMessage.builder().messageId(id).message(message).build());
        kafkaEntityTemplate.send(propertiesConfig.getERROR_MESSAGE_TOPIC(), error);
    }

}