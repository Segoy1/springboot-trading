package de.segoy.springboottradingweb.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@KafkaListener(groupId = "${kafka.consumer.group.id}",topics = {"${kafka.names.topic.errorMessage}", "${kafka.names" +
        ".topic" +
        ".optionMarketData}"})
public class ApiResponseKafkaHandler {
}
