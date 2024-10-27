package de.segoy.springboottradingdata.config;

import de.segoy.springboottradingdata.model.subtype.Symbol;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;


/**
 * This class contains the values that determine how The Autotrade Parameters are set.
 * Values that are not expected to change frequently are hardcoded. If they change more frequently add them to application.yml
 */
@Configuration
@Getter
public class TradeRuleSettingsConfig {

    @Value("${autotrade.delta}")
    private double delta;
    @Value("${autotrade.orderLimitValue}")
    private double limitValue;
    @Value("${autotrade.spreadSize}")
    private int spreadSize;

    private final double toleranceForOrderFill = 0.1;
    private final BigDecimal quantity = BigDecimal.ONE;
    private final Symbol tradeSymbol = Symbol.SPX;
    private final double sellThreshold = 2;



}
