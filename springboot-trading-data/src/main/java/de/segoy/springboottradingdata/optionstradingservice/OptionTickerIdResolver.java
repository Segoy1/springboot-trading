package de.segoy.springboottradingdata.optionstradingservice;

import com.ib.client.Types;
import de.segoy.springboottradingdata.constants.AutoDayTradeConstants;
import de.segoy.springboottradingdata.model.subtype.Symbol;
import de.segoy.springboottradingdata.service.IBKRTimeStampFormatter;
import java.sql.Timestamp;
import java.time.LocalDate;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OptionTickerIdResolver {

  @Builder
  public record OptionDetails(String date, Symbol symbol, Double strike, Types.Right right) {
    @Override
    public String toString() {
      return date
          + AutoDayTradeConstants.DELIMITER
          + symbol
          + AutoDayTradeConstants.DELIMITER
          + strike
          + AutoDayTradeConstants.DELIMITER
          + right;
    }
  }

  private final IBKRTimeStampFormatter ibkrTimeStampFormatter;

  public OptionDetails resolveTickerIdToOptionDetails(int tickerId) {

    Types.Right right = tickerId < 0 ? Types.Right.Put : Types.Right.Call;

    //TODO: when there are Fields that can have .5 Strikes check for symbol and adjust value accordingly
    double strike = Math.abs(tickerId % AutoDayTradeConstants.LAST_TRADE_DATE_TICKER_MULTIPLIER);

    String lastTradeDate = resolveDate(tickerId);

    Symbol symbol = resolveSymbol(tickerId);

    return OptionDetails.builder().date(lastTradeDate).symbol(symbol).strike(strike).right(right).build();
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
    int symbolValue = Math.abs(tickerId / AutoDayTradeConstants.SYMBOL_TICKER_MULTIPLIER);
    return Symbol.fromValue(symbolValue);
  }
}
