package de.segoy.springboottradingweb.spxautotrade.testdata;

import com.ib.client.Types;
import de.segoy.springboottradingdata.config.KafkaConstantsConfig;
import de.segoy.springboottradingdata.constants.AutoDayTradeConstants;
import de.segoy.springboottradingdata.model.data.kafka.KafkaDataType;
import de.segoy.springboottradingdata.model.data.kafka.OptionMarketData;
import de.segoy.springboottradingdata.model.subtype.Symbol;
import de.segoy.springboottradingdata.optionstradingservice.LastTradeDateBuilder;
import de.segoy.springboottradingdata.optionstradingservice.OptionTickerIdEncoder;
import de.segoy.springboottradingdata.optionstradingservice.OptionTickerIdResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TestStreamDataController {

  private final KafkaTemplate<String, KafkaDataType> kafkaTemplate;
  private final KafkaConstantsConfig kafkaConstantsConfig;
  private final LastTradeDateBuilder lastTradeDateBuilder;
  private final OptionTickerIdEncoder tickerIdEncoder;

  @GetMapping("/chainTestData")
  public void testData() {
    String date = lastTradeDateBuilder.getDateStringFromToday();
    double baseStrike = 5600;
    for (int p = 0; p < 2; p++) {
      Types.Right right = p == 0 ? Types.Right.Call : Types.Right.Put;
      for (int i = 0; i < 10; i++) {
        double strike = baseStrike + (i * 5);
        kafkaTemplate.send(
            kafkaConstantsConfig.getOPTION_MARKET_DATA_TOPIC(),
            date
                + AutoDayTradeConstants.DELIMITER
                + Symbol.SPX
                + AutoDayTradeConstants.DELIMITER
                + strike
                + AutoDayTradeConstants.DELIMITER
                + right,
            createTestData(date, right, strike));
      }
    }
  }

  private OptionMarketData createTestData(String date, Types.Right right, double strike) {
    return OptionMarketData.builder()
        .tickerId(tickerIdEncoder.encodeOptionTickerId(new OptionTickerIdResolver.OptionDetails(date,Symbol.SPX,
                strike,right)))
        .lastTradeDate(date)
        .symbol(Symbol.SPX)
        .field(AutoDayTradeConstants.CHAIN_SAVE_FIELD)
        .right(right)
        .strike(strike)
        .delta(0.3)
        .gamma(0.02)
        .vega(0.1)
        .theta(0.1)
        .optPrice(3.0)
        .build();
  }
}
