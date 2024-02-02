package de.segoy.springboottradingdata.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;


@Configuration
@Getter
public class KafkaConstantsConfig {


    @Value("${kafka.bootstrap-servers}")
    private String BOOTSTRAP_SERVERS;

    @Value("${kafka.consumer.group.id}")
    private String groupId;
    @Value("${kafka.consumer.rest.group.id}")
    private String restResponseGroupId;

    @Value("${kafka.names.topic.accountSummary}")
    private String ACCOUNT_SUMMARY_TOPIC;

    @Value("${kafka.names.topic.historicalData}")
    private String HISTORICAL_TOPIC;

    @Value("${kafka.names.topic.contractData}")
    private String CONTRACT_TOPIC;

    @Value("${kafka.names.topic.orderData}")
    private String ORDER_TOPIC;

    @Value("${kafka.names.topic.openOrders}")
    private String OPEN_ORDER_TOPIC;

    @Value("${kafka.names.topic.positions}")
    private String POSITION_TOPIC;

    @Value("${kafka.names.topic.errorMessage}")
    private String ERROR_MESSAGE_TOPIC;

    @Value("${kafka.names.topic.accountPnL}")
    private String ACCOUNT_PNL_TOPIC;

    @Value("${kafka.names.topic.singlePnL}")
    private String SINGLE_PNL_TOPIC;

    @Value("${kafka.names.topic.optionMarketData}")
    private String OPTION_MARKET_DATA_TOPIC;

    @Value("${kafka.names.topic.standardMarketData}")
    private String STANDARD_MARKET_DATA_TOPIC;
}
