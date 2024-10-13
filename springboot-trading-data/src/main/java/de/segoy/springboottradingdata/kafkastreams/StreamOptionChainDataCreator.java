package de.segoy.springboottradingdata.kafkastreams;

import com.ib.client.Types;
import de.segoy.springboottradingdata.constants.AutoDayTradeConstants;
import de.segoy.springboottradingdata.model.data.kafka.OptionChainData;
import de.segoy.springboottradingdata.model.data.kafka.OptionListData;
import de.segoy.springboottradingdata.model.data.kafka.OptionMarketData;
import org.springframework.stereotype.Service;

@Service
public class StreamOptionChainDataCreator {

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
        .lastTradeDate(Long.parseLong(marketData.getLastTradeDate()))
        .symbol(marketData.getSymbol())
        .puts(new OptionListData())
        .calls(new OptionListData())
        .build();
  }
}
