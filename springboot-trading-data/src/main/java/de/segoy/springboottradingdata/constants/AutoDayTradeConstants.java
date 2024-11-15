package de.segoy.springboottradingdata.constants;

public class AutoDayTradeConstants {
    public static final int CALL_TICKER_IDENTIFIER = 1;
    public static final int PUT_TICKER_IDENTIFIER = -1;
    public static final int LAST_TRADE_DATE_TICKER_MULTIPLIER = 10000;
    public static final int SYMBOL_TICKER_MULTIPLIER = 10000000;
    public static final int STRATEGY_TICKER_MULTIPLIER = 1000000;
    public static final String DELIMITER = "_";
    public static final String CHAIN_SAVE_FIELD = "modelOptComp";
    public static final double SPX_OPTION_PRICE_MULTIPLIER = 0.05;
    public static final double STOCK_OPTION_PRICE_MULTIPLIER = 0.01;

    private AutoDayTradeConstants() {}
}
