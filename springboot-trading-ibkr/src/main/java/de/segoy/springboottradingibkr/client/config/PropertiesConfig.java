package de.segoy.springboottradingibkr.client.config;

import lombok.Getter;
import lombok.Setter;
import lombok.Synchronized;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "app.ibkr")
@Configuration
public class PropertiesConfig {

    @Getter
    @Setter
    private boolean isReaderStarted;

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
