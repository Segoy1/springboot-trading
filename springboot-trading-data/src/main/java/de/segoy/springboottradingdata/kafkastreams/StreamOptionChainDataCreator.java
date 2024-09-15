package de.segoy.springboottradingdata.kafkastreams;

import com.ib.client.Types;
import de.segoy.springboottradingdata.constants.AutoDayTradeConstants;
import de.segoy.springboottradingdata.model.data.OptionChainData;
import de.segoy.springboottradingdata.model.data.OptionMarketData;
import de.segoy.springboottradingdata.optionstradingservice.OptionTickerIdResolver;
import java.util.HashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StreamOptionChainDataCreator {

  private final OptionTickerIdResolver optionTickerIdResolver;

  public OptionChainData buildChain(OptionMarketData marketData, OptionChainData aggregatedChain) {
    OptionTickerIdResolver.OptionDetails optionDetails =
        optionTickerIdResolver.resolveTickerIdToOptionDetails(marketData.getTickerId());
    if (aggregatedChain.getLastTradeDate() == null) {
      aggregatedChain = createNewChain(optionDetails);
    }

    if (marketData.getField().equals(AutoDayTradeConstants.CHAIN_SAVE_FIELD)) {
      if (optionDetails.right().equals(Types.Right.Call)) {
        aggregatedChain.getCalls().put(optionDetails.strike(), marketData);
      } else if (optionDetails.right().equals(Types.Right.Put)) {
        aggregatedChain.getPuts().put(optionDetails.strike(), marketData);
      }
    }

    return aggregatedChain;
  }

  private OptionChainData createNewChain(OptionTickerIdResolver.OptionDetails optionDetails) {
    return OptionChainData.builder()
        .lastTradeDate(optionDetails.date())
        .symbol(optionDetails.symbol().name())
        .puts(new HashMap<>())
        .calls(new HashMap<>())
        .build();
  }
}
