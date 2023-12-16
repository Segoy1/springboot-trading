package de.segoy.springboottradingdata.service.messagehandler;

import de.segoy.springboottradingdata.config.PropertiesConfig;
import de.segoy.springboottradingdata.model.AccountSummary;
import de.segoy.springboottradingdata.model.entity.IBKRDataTypeEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class AccountSummaryMessageHandler {

    private final KafkaTemplate<String, IBKRDataTypeEntity> kafkaEntityTemplate;
    private final PropertiesConfig propertiesConfig;

    public AccountSummaryMessageHandler(KafkaTemplate<String, IBKRDataTypeEntity> kafkaEntityTemplate, PropertiesConfig propertiesConfig) {
        this.kafkaEntityTemplate = kafkaEntityTemplate;
        this.propertiesConfig = propertiesConfig;
    }

    public void sendAccountSummaryMessage(AccountSummary accountSummary){
        kafkaEntityTemplate.send(propertiesConfig.getACCOUNT_SUMMARY_TOPIC(), accountSummary);
    }
}
