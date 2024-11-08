package de.segoy.springboottradingdata.optionstradingservice;

import de.segoy.springboottradingdata.model.subtype.Symbol;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AutotradeDbAndTickerIdEncoder {

  public static final int MULTIPLIER = 1000000;
  private final LastTradeDateBuilder lastTradeDateBuilder;

  public int generateIntIdForTodayBySymbol(Symbol symbol) {
    return  generateLongIdIdForTodayBySymbol(symbol).intValue();
  }
  public Long generateLongIdIdForTodayBySymbol(Symbol symbol) {
      return generateLongIdForLastTradeDateAndSymbold(lastTradeDateBuilder.getDateLongFromToday(),symbol);
  }
  public Long generateLongIdForLastTradeDateAndSymbold(Long lastTradeDate, Symbol symbol) {
    return lastTradeDate + ((long) symbol.numberValue() * MULTIPLIER);
  }
}
