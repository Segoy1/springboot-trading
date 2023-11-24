package de.segoy.springboottradingibkr.client.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "app.ibkr")
@Configuration
@Getter
@Setter
public class PropertiesConfig {

    private boolean isReaderStarted;
    private Integer nextValidOrderId;
}
