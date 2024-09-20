package de.segoy.springboottradingweb.spxautotrade.settings;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class TradeRuleSettingsConfig {

    @Value("${autotrade.delta}")
    private double delta;
    @Value("${autotrade.orderLimitValue}")
    private double limitValue;
    @Value("${autotrade.spreadSize}")
    private int spreadSize;



}
