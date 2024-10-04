package de.segoy.springboottradingdata.kafkastreams;

import com.ib.client.Types;
import de.segoy.springboottradingdata.constants.AutoDayTradeConstants;
import de.segoy.springboottradingdata.model.data.OptionChainData;
import de.segoy.springboottradingdata.model.data.OptionMarketData;
import de.segoy.springboottradingdata.optionstradingservice.OptionTickerIdResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StreamOptionChainDataCreator {

  private final OptionTickerIdResolver optionTickerIdResolver;

  public OptionChainData buildChain(OptionMarketData marketData, OptionChainData aggregatedChain) {
    if (aggregatedChain.getLastTradeDate() == null) {
      aggregatedChain = createNewChain(marketData);
    }

    if (marketData.getField().equals(AutoDayTradeConstants.CHAIN_SAVE_FIELD)) {
      if (marketData.getRight().equals(Types.Right.Call)) {
        aggregatedChain.getCalls().put(marketData.getStrike(), marketData);
      } else if (marketData.getRight().equals(Types.Right.Put)) {
        aggregatedChain.getPuts().put(marketData.getStrike(), marketData);
      }
    }

    return aggregatedChain;
  }

  private OptionChainData createNewChain(OptionMarketData marketData) {
    return OptionChainData.builder()
        .lastTradeDate(marketData.getLastTradeDate())
        .symbol(marketData.getSymbol())
        .build();
  }
}
