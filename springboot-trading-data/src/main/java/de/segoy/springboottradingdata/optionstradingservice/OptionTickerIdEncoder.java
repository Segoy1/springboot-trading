package de.segoy.springboottradingdata.optionstradingservice;

import com.ib.client.Types;
import de.segoy.springboottradingdata.constants.AutoDayTradeConstants;
import de.segoy.springboottradingdata.model.data.entity.ContractData;
import de.segoy.springboottradingdata.model.subtype.Symbol;
import de.segoy.springboottradingdata.service.IBKRTimeStampFormatter;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OptionTickerIdEncoder {

  private final IBKRTimeStampFormatter ibkrTimeStampFormatter;

  /**
   * Encodes the Contract Data for options to a ticker Id where the the last 4 digits are the strike
   * price 7th last to 5th last digits are the days from today (maximal 999 days from today) and the
   * first 3 digits (maximal 214) Signify the symbol with its value determined in @{@link Symbol}
   * negative value if it is a put positive if call
   *
   * @param contractData
   * @return tickerId so the IBkR API can work with it
   */
  public int encodeOptionTickerId(ContractData contractData) {
    return encode(
        contractData.getRight(),
        contractData.getStrike().intValue(),
        contractData.getLastTradeDate(),
        contractData.getSymbol());
  }

  public int encodeOptionTickerId(OptionTickerIdResolver.OptionDetails details) {
    int strike;
    if (details.strike() % 1 == 0) {
      strike = details.strike().intValue();
    } else {
      strike = Double.valueOf(details.strike() * 10).intValue();
    }
    return encode(details.right(), strike, details.date(), details.symbol());
  }

  private int encode(Types.Right right, int strike, String date, Symbol symbol) {
    return (encodeSymbol(symbol) + encodeLastTradeDate(date) + strike) * encodeRight(right);
  }

  private int encodeRight(Types.Right right) {
    if (right.equals(Types.Right.Call)) {
      return AutoDayTradeConstants.CALL_TICKER_IDENTIFIER;
    }
    if (right.equals(Types.Right.Put)) {
      return AutoDayTradeConstants.PUT_TICKER_IDENTIFIER;
    } else {
      throw new RuntimeException("No Right defined in Contract Data");
    }
  }

  private int encodeLastTradeDate(String lastTradeDate) {
    Timestamp date = ibkrTimeStampFormatter.formatStringToTimeStamp(lastTradeDate);
    //    return
    // (int)Duration.between(LocalDate.now().atStartOfDay(),date.toLocalDateTime()).get(ChronoUnit.DAYS);
    long days = ChronoUnit.DAYS.between(LocalDate.now().atStartOfDay(), date.toLocalDateTime());
    return (int) days * AutoDayTradeConstants.LAST_TRADE_DATE_TICKER_MULTIPLIER;
  }

  private int encodeSymbol(Symbol symbol) {
    return symbol.numberValue() * AutoDayTradeConstants.SYMBOL_TICKER_MULTIPLIER;
  }
}
