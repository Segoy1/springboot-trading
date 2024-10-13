package de.segoy.springboottradingdata.modelconverter;

import de.segoy.springboottradingdata.model.data.entity.OptionChainDataDBO;
import de.segoy.springboottradingdata.model.data.entity.OptionListDBO;
import de.segoy.springboottradingdata.model.data.entity.OptionMarketDataDBO;
import de.segoy.springboottradingdata.model.data.kafka.KafkaOptionChainData;
import de.segoy.springboottradingdata.model.data.kafka.KafkaOptionMarketData;
import de.segoy.springboottradingdata.repository.OptionChainDataRepository;
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
public class OptionChainDataToDBO {

  public static final int NEGATIVE_FOR_PUTS = -1;
  private final OptionMarketDataRepository optionMarketDataRepository;
  private final OptionListRepository optionListRepository;
  private final OptionChainDataRepository optionChainDataRepository;

  @Transactional
  public OptionChainDataDBO convert(KafkaOptionChainData kafkaOptionChainData) {
    List<OptionMarketDataDBO> callList = mapToList(kafkaOptionChainData.getCalls().getOptions());
    List<OptionMarketDataDBO> putList = mapToList(kafkaOptionChainData.getPuts().getOptions());
    OptionListDBO callsDBO =
        optionListRepository.save(
            OptionListDBO.builder()
                .id(kafkaOptionChainData.getLastTradeDate())
                .optionList(callList)
                .build());
    OptionListDBO putsDBO =
        optionListRepository.save(
            OptionListDBO.builder()
                .id(kafkaOptionChainData.getLastTradeDate() * -1)
                .optionList(putList)
                .build());
    return optionChainDataRepository.save(
        OptionChainDataDBO.builder()
            .lastTradeDate(kafkaOptionChainData.getLastTradeDate())
            .symbol(kafkaOptionChainData.getSymbol())
            .calls(callsDBO)
            .puts(putsDBO)
            .build());
  }

  private List<OptionMarketDataDBO> mapToList(Map<Double, KafkaOptionMarketData> map) {
    List<OptionMarketDataDBO> list = new ArrayList<>();
    map.forEach(
        (key, value) -> {
          list.add(toOptionMarketDataDBO(value));
        });
    return list;
  }

  private OptionMarketDataDBO toOptionMarketDataDBO(KafkaOptionMarketData optionMarketData) {
    return optionMarketDataRepository.save(
        OptionMarketDataDBO.builder()
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
