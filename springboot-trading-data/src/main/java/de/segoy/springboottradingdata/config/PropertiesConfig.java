package de.segoy.springboottradingdata.config;

import lombok.Getter;
import lombok.Synchronized;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

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

    @Synchronized
    public Long getNextValidOrderId(){
        return nextValidOrderId;
    }

    @Synchronized
    public void setNextValidOrderId(Long nextValidOrderId) {
        this.nextValidOrderId = nextValidOrderId;
    }
}
