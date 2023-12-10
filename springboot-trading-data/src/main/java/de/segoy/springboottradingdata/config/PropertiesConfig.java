package de.segoy.springboottradingdata.config;

import lombok.Getter;
import lombok.Synchronized;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.HashSet;
import java.util.Set;

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



    private final Set<Long> activeApiCalls = new HashSet<>();

    @Synchronized
    public Long getNextValidOrderId(){
        return nextValidOrderId;
    }

    @Synchronized
    public void setNextValidOrderId(Long nextValidOrderId) {
        this.nextValidOrderId = nextValidOrderId;
    }

    @Synchronized
    public void addToActiveApiCalls(Long id){
        activeApiCalls.add(id);
    }
    @Synchronized
    public Set<Long> getActiveApiCalls(){
        return activeApiCalls;
    }
    @Synchronized
    public void removeFromActiveApiCalls(Long id){
        activeApiCalls.remove(id);
    }
}
