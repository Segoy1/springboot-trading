package de.segoy.springboottradingdata.config;

import lombok.Getter;
import lombok.Synchronized;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.time.Instant;
import java.util.*;

@Configuration
public class PropertiesConfig {



    @Value("${app.ibkr.nextValidOrderId}")
    private Long nextValidOrderId;

    @Getter
    @Value("${app.ibkr.dateTimeFormat}")
    private String dateTimeFormat;

    @Getter
    @Value("${app.ibkr.dateFormat}")
    private String dateFormat;

    @Getter
    @Value("${app.ibkr.connectionId}")
    private int connectionId;

    @Getter
    @Value("${app.ibkr.generic.ticks}")
    private String genericTicks;

    @Getter
    @Value("${spring.kafka.names.topic.historicalData}")
    private String HISTORICAL_TOPIC;

    @Getter
    @Value("${spring.kafka.names.topic.contractData}")
    private String CONTRACT_TOPIC;

    @Getter
    @Value("${spring.kafka.names.topic.orderData}")
    private String ORDER_TOPIC;

    @Getter
    @Value("${spring.kafka.names.topic.positionData}")
    private String POSITION_TOPIC;

    @Getter
    @Value("${spring.kafka.names.topic.twsMessage}")
    private String TWS_MESSAGE_TOPIC;

    @Getter
    @Value("${spring.kafka.names.topic.errorMessage}")
    private String ERROR_MESSAGE_TOPIC;

    @Getter
    @Value("${spring.kafka.bootstrap-servers}")
    private String BOOTSTRAP_SERVERS;

    @Getter
    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;

    @Getter
    @Value("${app.constants.positions.call.id}")
    private int positionsCallId;

    @Getter
    @Value("${app.constants.time.millis}")
    private String addMillis;


    private final List<Integer> activeMarketData = new ArrayList<>();

    @Synchronized
    public Long getNextValidOrderId(){
        return nextValidOrderId;
    }

    @Synchronized
    public void setNextValidOrderId(Long nextValidOrderId) {
        this.nextValidOrderId = nextValidOrderId;
    }

    @Synchronized
    public void addToActiveMarketData(int id){
        activeMarketData.add(id);
    }
    @Synchronized
    public List<Integer> getActiveMarketData(){
        return activeMarketData;
    }
    @Synchronized
    public void removeFromActiveMarketData(int id){
        activeMarketData.remove(id);
    }


    public Date getTwoSecondsAgo(){
        return Date.from(Instant.now().minusSeconds(2L));
    }
}
