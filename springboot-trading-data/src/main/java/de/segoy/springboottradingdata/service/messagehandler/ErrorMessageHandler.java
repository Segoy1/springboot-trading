package de.segoy.springboottradingdata.service.messagehandler;

import de.segoy.springboottradingdata.config.KafkaConstantsConfig;
import de.segoy.springboottradingdata.model.data.IBKRDataType;
import de.segoy.springboottradingdata.model.data.message.ErrorMessage;
import de.segoy.springboottradingdata.repository.message.ErrorMessageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ErrorMessageHandler {

    private final ErrorMessageRepository errorMessageRepository;
    private final KafkaTemplate<String, IBKRDataType> kafkaEntityTemplate;
    private final KafkaConstantsConfig kafkaConstantsConfig;

    public ErrorMessageHandler(ErrorMessageRepository errorMessageRepository, KafkaTemplate<String,
            IBKRDataType> kafkaEntityTemplate, KafkaConstantsConfig kafkaConstantsConfig) {
        this.errorMessageRepository = errorMessageRepository;
        this.kafkaEntityTemplate = kafkaEntityTemplate;
        this.kafkaConstantsConfig = kafkaConstantsConfig;
    }

    public void handleError(ErrorMessage errorMessage) {
        log.warn(errorMessage.getMessage());
        ErrorMessage error = errorMessageRepository.save(errorMessage);
        kafkaEntityTemplate.send(kafkaConstantsConfig.getERROR_MESSAGE_TOPIC(), error);
    }

}
