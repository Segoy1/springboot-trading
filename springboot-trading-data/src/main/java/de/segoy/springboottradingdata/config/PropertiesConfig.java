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



    private final Set<Integer> activeApiCalls = new HashSet<>();

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
    public void addToActiveApiCalls(int id){
        activeApiCalls.add(id);
    }
    @Synchronized
    public Set<Integer> getActiveApiCalls(){
        return activeApiCalls;
    }
    @Synchronized
    public void removeFromActiveApiCalls(int id){
        activeApiCalls.remove(id);
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

    public String addMillis(){
        return "000";
    }

    public Date getTwoSecondsAgo(){
        return Date.from(Instant.now().minusSeconds(2L));
    }
}
