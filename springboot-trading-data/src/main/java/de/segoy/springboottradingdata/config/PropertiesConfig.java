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
    @Value("${app.ibkr.tradingPort}")
    private int tradingPort;

    @Getter
    @Value("${app.ibkr.connectionId}")
    private long connectionId;

    @Getter
    @Value("${app.ibkr.pnl.accountId}")
    private int pnlAccountId;

    @Getter
    @Value("${app.ibkr.generic.ticks}")
    private String genericTicks;

    @Getter
    @Value("${app.constants.positions.call.id}")
    private int POSITION_CALL_ID;

    @Getter
    @Value("${app.constants.open.orders.id}")
    private int OPEN_ORDERS_ID;
    @Getter
    @Value("${app.constants.account.summary.id}")
    private int ACCOUNT_SUMMARY_ID;
    @Getter
    @Value("${app.ibkr.accountSummary.tags.AccruedCash}")
    private String ACCRUED_CASH;
    @Getter
    @Value("${app.ibkr.accountSummary.tags.BuyingPower}")
    private String BUYING_POWER;
    @Getter
    @Value("${app.ibkr.accountSummary.tags.NetLiquidation}")
    private String NET_LIQUIDATION;


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
