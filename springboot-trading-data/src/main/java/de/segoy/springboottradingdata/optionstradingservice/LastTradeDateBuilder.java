package de.segoy.springboottradingdata.optionstradingservice;

import de.segoy.springboottradingdata.service.IBKRTimeStampFormatter;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class LastTradeDateBuilder {

  private final IBKRTimeStampFormatter ibkrTimeStampFormatter;

  public String getDateStringFromToday() {
    return ibkrTimeStampFormatter.formatTimestampToDate(Timestamp.valueOf(LocalDateTime.now()));
  }

  public int getDateIntFromToday() {
    return Integer.parseInt(getDateStringFromToday());
  }
}
