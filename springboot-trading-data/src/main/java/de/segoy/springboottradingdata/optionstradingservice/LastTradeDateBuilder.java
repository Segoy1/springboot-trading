package de.segoy.springboottradingdata.optionstradingservice;

import de.segoy.springboottradingdata.service.IBKRTimeStampFormatter;
import java.sql.Timestamp;
import java.time.LocalDate;
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
    if (LocalDate.now().getDayOfWeek().getValue() < 5) {
      return ibkrTimeStampFormatter.formatTimestampToDate(Timestamp.valueOf(LocalDateTime.now()));
    } else {
      // Test Code for weekends
      return ibkrTimeStampFormatter.formatTimestampToDate(
          Timestamp.valueOf(LocalDateTime.now().plusDays(2)));
    }
  }

  public String getDateStringFromTomorrow() {
    return ibkrTimeStampFormatter.formatTimestampToDate(
        Timestamp.valueOf(LocalDateTime.now().plusDays(1)));
  }

  public int getDateIntFromToday() {
    return Integer.parseInt(getDateStringFromToday());
  }
}
