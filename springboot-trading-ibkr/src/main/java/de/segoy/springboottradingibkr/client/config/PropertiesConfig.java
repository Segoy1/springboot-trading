package de.segoy.springboottradingibkr.client.config;

import lombok.Getter;
import lombok.Setter;
import lombok.Synchronized;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PropertiesConfig {

    @Getter
    @Setter
    @Value("${app.ibkr.isReaderStarted}")
    private boolean isReaderStarted;

    @Value("${app.ibkr.nextValidOrderId}")
    private Integer nextValidOrderId;

    @Synchronized
    public Integer getNextValidOrderId(){
        return nextValidOrderId;
    }

    @Synchronized
    public void setNextValidOrderId(Integer nextValidOrderId) {
        this.nextValidOrderId = nextValidOrderId;
    }
}
