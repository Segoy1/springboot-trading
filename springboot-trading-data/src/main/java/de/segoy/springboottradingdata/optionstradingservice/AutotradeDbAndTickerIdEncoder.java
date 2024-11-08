package de.segoy.springboottradingdata.optionstradingservice;

import de.segoy.springboottradingdata.constants.AutoDayTradeConstants;
import de.segoy.springboottradingdata.model.subtype.Strategy;
import de.segoy.springboottradingdata.model.subtype.Symbol;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AutotradeDbAndTickerIdEncoder {

  private final LastTradeDateBuilder lastTradeDateBuilder;

  public int generateIntIdForTodayBySymbol(Symbol symbol) {
    return generateLongIdIdForTodayBySymbol(symbol).intValue();
  }

  public Long generateLongIdIdForTodayBySymbol(Symbol symbol) {
    return generateLongIdForLastTradeDateAndSymbold(
        lastTradeDateBuilder.getDateLongFromToday(), symbol);
  }

  public Long generateLongIdForLastTradeDateAndSymbold(Long lastTradeDate, Symbol symbol) {
    return lastTradeDate
        + ((long) symbol.numberValue() * AutoDayTradeConstants.SYMBOL_TICKER_MULTIPLIER);
  }

  public int generateIntForLastTradeDateBySymbolAndStrategy(
      Long lastTradeDate, Symbol symbol, Strategy strategy) {
    if (strategy.equals(Strategy.IRON_CONDOR)) {
      return generateLongIdForLastTradeDateAndSymbold(lastTradeDate, symbol).intValue()
          + (strategy.numberValue() * AutoDayTradeConstants.STRATEGY_TICKER_MULTIPLIER);
    } else {
      throw new IllegalArgumentException(
          "Strategy should be IRON_CONDOR or Implement Logic for others");
    }
  }

  public Long generateLongForTodayBySymbolAndStrategy(Symbol symbol, Strategy strategy) {
    return (long)
        generateIntForLastTradeDateBySymbolAndStrategy(
            lastTradeDateBuilder.getDateLongFromToday(), symbol, strategy);
  }
}
