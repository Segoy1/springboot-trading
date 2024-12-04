package de.segoy.springboottradingdata.optionstradingservice;

import de.segoy.springboottradingdata.constants.AutoDayTradeConstants;
import de.segoy.springboottradingdata.model.subtype.Strategy;
import de.segoy.springboottradingdata.model.subtype.Symbol;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
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

  public Integer generateIntForLastTradeDateBySymbolAndStrategy(
      Long lastTradeDate, Symbol symbol, Strategy strategy) {
    if (strategy.equals(Strategy.IRON_CONDOR)) {
      return generateLongIdForLastTradeDateAndSymbold(lastTradeDate, symbol).intValue()
          + (strategy.numberValue() * AutoDayTradeConstants.STRATEGY_TICKER_MULTIPLIER);
    } else {
     log.error("Strategy should be IRON_CONDOR or Implement Logic for others");
     //Todo better return value
     return null;
    }
  }

  public Long generateLongForTodayBySymbolAndStrategy(Symbol symbol, Strategy strategy) {
    return Long.valueOf(
        generateIntForLastTradeDateBySymbolAndStrategy(
            lastTradeDateBuilder.getDateLongFromToday(), symbol, strategy));
  }
}
