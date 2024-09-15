package de.segoy.springboottradingdata.service;

import de.segoy.springboottradingdata.constants.AutoDayTradeConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class TickerIDResolveService {

  private final LastTradeDateBuilder lastTradeDateBuilder;

  public String resolveTickerIdToString(int tickerId){
    Map<String, String> map = resolveTickerIdToMap(tickerId);
    if(map.get("tickerId").isEmpty()){
      return map.get("Date")+AutoDayTradeConstants.DELIMITER+map.get("Symbol")+AutoDayTradeConstants.DELIMITER+map.get(
              "Strike")+AutoDayTradeConstants.DELIMITER+map.get("Action");
    }else{
      return map.get("tickerId");
    }
  }

  public Map<String, String> resolveTickerIdToMap(int tickerID) {
    Map<String, String> tickerIDMap = new HashMap<>();
    if (tickerID >= AutoDayTradeConstants.SPX_TODAY_TICKER_IDENTIFIER
        && tickerID
            <= AutoDayTradeConstants.SPX_TODAY_TICKER_IDENTIFIER
                + AutoDayTradeConstants.EXPECTED_MAX_VALUE_OF_SPX) {
      tickerIDMap.put("Date", lastTradeDateBuilder.getDateStringFromToday());
      tickerIDMap.put("Symbol", AutoDayTradeConstants.SPX);
      tickerToStrikeAndOptionType(tickerIDMap, tickerID);

    } else {
      tickerIDMap.put("tickerId", tickerID + "");
    }
    return tickerIDMap;
  }

  private void tickerToStrikeAndOptionType(Map<String, String> tickerIDMap, int tickerId) {
    int strikeWithPutCallInfo = tickerId - AutoDayTradeConstants.SPX_TODAY_TICKER_IDENTIFIER;
    if (strikeWithPutCallInfo % 5 == 1) {
      int callStrike = strikeWithPutCallInfo - AutoDayTradeConstants.CALL_TICKER_IDENTIFIER;
      tickerIDMap.put("Strike", callStrike + "");
      tickerIDMap.put("Action", AutoDayTradeConstants.CALL);
    } else if (strikeWithPutCallInfo % 5 == 4) {
      int putStrike = strikeWithPutCallInfo - AutoDayTradeConstants.PUT_TICKER_IDENTIFIER;
      tickerIDMap.put("Strike", putStrike + "");
      tickerIDMap.put("Action", AutoDayTradeConstants.PUT);
    } else {
      throw new RuntimeException("Invalid strike Price number");
    }
  }
}
