package de.segoy.springboottradingdata.service.messagehandler;

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

    public TwsMessageHandler(TwsMessageRepository twsMessageRepository,
                             KafkaTemplate<String, IBKRDataTypeEntity> kafkaEntityTemplate) {
        this.twsMessageRepository = twsMessageRepository;
        this.kafkaEntityTemplate = kafkaEntityTemplate;
    }

    public void handleMessage(TwsMessage twsMessage) {
        log.info(twsMessage.getMessage());
        TwsMessage savedTwsMessage = twsMessageRepository.save(twsMessage);
        kafkaEntityTemplate.send(savedTwsMessage.getTopic(), savedTwsMessage);
        //Change to Submit with Key
//        kafkaEntityTemplate.send(savedTwsMessage.getTopic(), savedTwsMessage.getMessageId().toString(), savedTwsMessage);
    }
}
