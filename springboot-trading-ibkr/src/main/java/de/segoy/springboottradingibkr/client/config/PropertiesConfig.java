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
    private Long nextValidOrderId;

    @Synchronized
    public Long getNextValidOrderId(){
        return nextValidOrderId;
    }

    @Synchronized
    public void setNextValidOrderId(Long nextValidOrderId) {
        this.nextValidOrderId = nextValidOrderId;
    }
}
