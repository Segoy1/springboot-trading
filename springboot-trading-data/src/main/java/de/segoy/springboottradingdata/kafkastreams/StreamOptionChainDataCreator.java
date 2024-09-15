package de.segoy.springboottradingdata.kafkastreams;

import de.segoy.springboottradingdata.constants.AutoDayTradeConstants;
import de.segoy.springboottradingdata.model.data.OptionChainData;
import de.segoy.springboottradingdata.model.data.OptionMarketData;
import de.segoy.springboottradingdata.service.TickerIDResolveService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StreamOptionChainDataCreator {

  private final TickerIDResolveService tickerIDResolveService;

  public OptionChainData buildChain(
      String key, OptionMarketData marketData, OptionChainData aggregatedChain) {
    if (aggregatedChain.getLastTradeDate() == null) {
      aggregatedChain = createNewChain(key);
    }
    Map<String,String> optionDetails = tickerIDResolveService.resolveTickerIdToMap(marketData.getTickerId());
//    marketData.


    return aggregatedChain;
  }

  private OptionChainData createNewChain(String key) {
    String[] keyParts = key.split(AutoDayTradeConstants.DELIMITER);
    return OptionChainData.builder()
        .lastTradeDate(keyParts[0])
        .symbol(keyParts[1])
        .puts(new HashSet<>())
        .calls(new HashSet<>())
        .build();
  }
}
