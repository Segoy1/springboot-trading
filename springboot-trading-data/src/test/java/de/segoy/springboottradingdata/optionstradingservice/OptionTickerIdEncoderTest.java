package de.segoy.springboottradingdata.optionstradingservice;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.ib.client.Types;
import de.segoy.springboottradingdata.model.data.entity.ContractDataDBO;
import de.segoy.springboottradingdata.model.subtype.Symbol;
import de.segoy.springboottradingdata.service.IBKRTimeStampFormatter;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OptionTickerIdEncoderTest {

  @Mock IBKRTimeStampFormatter ibkrTimeStampFormatter;
  @InjectMocks OptionTickerIdEncoder optionTickerIdEncoder;

  @Test
  void testOptionTickerIdEncoder_WithCall() {
    ContractDataDBO contractDataDBO =
        ContractDataDBO.builder()
            .right(Types.Right.Call)
            .lastTradeDate("20260916")
            .strike(BigDecimal.valueOf(5555))
            .symbol(Symbol.SPX)
            .build();
    Timestamp date = Timestamp.valueOf(LocalDate.of(2026, 9, 16).atStartOfDay());
    when(ibkrTimeStampFormatter.formatStringToTimeStamp("20260916")).thenReturn(date);

    int days =
        (int) ChronoUnit.DAYS.between(LocalDate.now().atStartOfDay(), date.toLocalDateTime());
    int expected = 10005555 + (days * 10000);
    int result = optionTickerIdEncoder.encodeOptionTickerId(contractDataDBO);

    assertEquals(expected, result);
  }

  @Test
  void testOptionTickerIdEncoder_WithPut() {
    ContractDataDBO contractDataDBO =
        ContractDataDBO.builder()
            .right(Types.Right.Put)
            .lastTradeDate("20260916")
            .strike(BigDecimal.valueOf(553))
            .symbol(Symbol.SPX)
            .build();
    Timestamp date = Timestamp.valueOf(LocalDate.of(2026, 9, 16).atStartOfDay());
    when(ibkrTimeStampFormatter.formatStringToTimeStamp("20260916")).thenReturn(date);

    int days =
        (int) ChronoUnit.DAYS.between(LocalDate.now().atStartOfDay(), date.toLocalDateTime());
    int expected = (10000553 + (days * 10000)) * -1;
    int result = optionTickerIdEncoder.encodeOptionTickerId(contractDataDBO);

    assertEquals(expected, result);
  }

  @Test
  void testOptionTickerIdEncoder_WithPut_AndXOISymbol() {
    ContractDataDBO contractDataDBO =
        ContractDataDBO.builder()
            .right(Types.Right.Put)
            .lastTradeDate("20260916")
            .strike(BigDecimal.valueOf(553))
            .symbol(Symbol.XOI)
            .build();
    Timestamp date = Timestamp.valueOf(LocalDate.of(2026, 9, 16).atStartOfDay());
    when(ibkrTimeStampFormatter.formatStringToTimeStamp("20260916")).thenReturn(date);

    int days =
        (int) ChronoUnit.DAYS.between(LocalDate.now().atStartOfDay(), date.toLocalDateTime());
    int expected = (100000553 + (days * 10000)) * -1;
    int result = optionTickerIdEncoder.encodeOptionTickerId(contractDataDBO);

    assertEquals(expected, result);
  }
}
