package de.segoy.springboottradingdata.optionstradingservice;

import com.ib.client.Types;
import de.segoy.springboottradingdata.constants.AutoDayTradeConstants;
import de.segoy.springboottradingdata.model.subtype.Symbol;
import de.segoy.springboottradingdata.service.IBKRTimeStampFormatter;
import java.sql.Timestamp;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OptionTickerIdResolver {

  public record OptionDetails(String date, Symbol symbol, Integer strike, Types.Right right) {}

  private final IBKRTimeStampFormatter ibkrTimeStampFormatter;

  public String resolveTickerIdToString(int tickerId) {
    OptionDetails details = resolveTickerIdToOptionDetails(tickerId);

    return details.date
        + AutoDayTradeConstants.DELIMITER
        + details.symbol
        + AutoDayTradeConstants.DELIMITER
        + details.strike
        + AutoDayTradeConstants.DELIMITER
        + details.right;
  }

  public OptionDetails resolveTickerIdToOptionDetails(int tickerId) {

    Types.Right right = tickerId < 0 ? Types.Right.Put : Types.Right.Call;

    int strike = tickerId % AutoDayTradeConstants.LAST_TRADE_DATE_TICKER_MULTIPLIER;

    String lastTradeDate = resolveDate(tickerId);

    Symbol symbol = resolveSymbol(tickerId);

    return new OptionDetails(lastTradeDate, symbol, strike, right);
  }

  private String resolveDate(int tickerId) {
    int daysDivisor =
        AutoDayTradeConstants.SYMBOL_TICKER_MULTIPLIER
            / AutoDayTradeConstants.LAST_TRADE_DATE_TICKER_MULTIPLIER;

    int removedStrike = (int) tickerId / AutoDayTradeConstants.LAST_TRADE_DATE_TICKER_MULTIPLIER;

    int days = removedStrike % daysDivisor;

    return ibkrTimeStampFormatter.formatTimestampToDate(
        Timestamp.valueOf(LocalDate.now().atStartOfDay().plusDays(days)));
  }

  private Symbol resolveSymbol(int tickerId) {
    int symbolValue = tickerId / AutoDayTradeConstants.SYMBOL_TICKER_MULTIPLIER;
    return Symbol.fromValue(symbolValue);
  }
}
