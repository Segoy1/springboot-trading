package de.segoy.springboottradingdata.modelconverter;

import de.segoy.springboottradingdata.model.data.entity.OptionChainDbo;
import de.segoy.springboottradingdata.model.data.entity.OptionListDbo;
import de.segoy.springboottradingdata.model.data.entity.OptionMarketDataDbo;
import de.segoy.springboottradingdata.model.data.kafka.OptionChainData;
import de.segoy.springboottradingdata.model.data.kafka.OptionMarketData;
import de.segoy.springboottradingdata.repository.OptionChainRepository;
import de.segoy.springboottradingdata.repository.OptionListRepository;
import de.segoy.springboottradingdata.repository.OptionMarketDataRepository;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OptionChainDataToDbo {

  public static final int NEGATIVE_FOR_PUTS = -1;
  private final OptionMarketDataRepository optionMarketDataRepository;
  private final OptionListRepository optionListRepository;
  private final OptionChainRepository optionChainRepository;

  @Transactional
  public OptionChainDbo convertAndSave(OptionChainData optionChainData) {
    List<OptionMarketDataDbo> callList = mapToList(optionChainData.getCalls().getOptions());
    List<OptionMarketDataDbo> putList = mapToList(optionChainData.getPuts().getOptions());
    OptionListDbo callsDBO =
        optionListRepository.save(
            OptionListDbo.builder()
                .id(optionChainData.getLastTradeDate())
                .optionList(callList)
                .build());
    OptionListDbo putsDBO =
        optionListRepository.save(
            OptionListDbo.builder()
                .id(optionChainData.getLastTradeDate() * -1)
                .optionList(putList)
                .build());
    return optionChainRepository.save(
        OptionChainDbo.builder()
            .lastTradeDate(optionChainData.getLastTradeDate())
            .symbol(optionChainData.getSymbol())
            .calls(callsDBO)
            .puts(putsDBO)
            .build());
  }

  private List<OptionMarketDataDbo> mapToList(Map<Double, OptionMarketData> map) {
    List<OptionMarketDataDbo> list = new ArrayList<>();
    map.forEach(
        (key, value) -> {
          list.add(toOptionMarketDataDBO(value));
        });
    return list;
  }

  private OptionMarketDataDbo toOptionMarketDataDBO(OptionMarketData optionMarketData) {
    return optionMarketDataRepository.save(
        OptionMarketDataDbo.builder()
            .tickerId((long) optionMarketData.getTickerId())
            .strike(optionMarketData.getStrike())
            .right(optionMarketData.getRight())
            .symbol(optionMarketData.getSymbol())
            .lastTradeDate(optionMarketData.getLastTradeDate())
            .field(optionMarketData.getField())
            .tickAttrib(optionMarketData.getTickAttrib())
            .impliedVol(optionMarketData.getImpliedVol())
            .delta(optionMarketData.getDelta())
            .optPrice(optionMarketData.getOptPrice())
            .pvDividend(optionMarketData.getPvDividend())
            .gamma(optionMarketData.getGamma())
            .vega(optionMarketData.getVega())
            .theta(optionMarketData.getTheta())
            .undPrice(optionMarketData.getUndPrice())
            .build());
  }
}
