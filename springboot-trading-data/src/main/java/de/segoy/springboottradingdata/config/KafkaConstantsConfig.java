package de.segoy.springboottradingdata.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;


@Configuration
@Getter
public class KafkaConstantsConfig {


    @Value("${spring.kafka.bootstrap-servers}")
    private String BOOTSTRAP_SERVERS;

    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;

    @Value("${spring.kafka.names.topic.accountSummary}")
    private String ACCOUNT_SUMMARY_TOPIC;

    @Value("${spring.kafka.names.topic.historicalData}")
    private String HISTORICAL_TOPIC;

    @Value("${spring.kafka.names.topic.contractData}")
    private String CONTRACT_TOPIC;

    @Value("${spring.kafka.names.topic.orderData}")
    private String ORDER_TOPIC;

    @Value("${spring.kafka.names.topic.openOrders}")
    private String OPEN_ORDER_TOPIC;

    @Value("${spring.kafka.names.topic.positions}")
    private String POSITION_TOPIC;

    @Value("${spring.kafka.names.topic.errorMessage}")
    private String ERROR_MESSAGE_TOPIC;

    @Value("${spring.kafka.names.topic.accountPnL}")
    private String ACCOUNT_PNL;
}
