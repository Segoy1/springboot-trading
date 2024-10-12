package de.segoy.springboottradingweb.spxautotrade;

import de.segoy.springboottradingdata.model.data.entity.ContractDataDBO;
import de.segoy.springboottradingdata.model.data.kafka.KafkaOptionChainData;
import de.segoy.springboottradingdata.model.subtype.Symbol;
import de.segoy.springboottradingdata.optionstradingservice.LastTradeDateBuilder;
import de.segoy.springboottradingibkr.client.service.marketdata.AutoTradeMarketDataService;
import de.segoy.springboottradingweb.spxautotrade.service.AutoTradeChainDataStopLiveDataService;
import de.segoy.springboottradingweb.spxautotrade.service.ChainDataContractDataCreateService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AutoTradeOptionChainCollector {

  public static final int MININUM_CHAIN_SIZE = 10;
  private final LastTradeDateBuilder lastTradeDateBuilder;
  private final ChainDataContractDataCreateService chainDataContractDataCreateService;
  private final AutoTradeMarketDataService autoTradeMarketDataService;
  private final AutoTradeChainDataStopLiveDataService autoTradeChainDataStopLiveDataService;

  @Transactional
  @KafkaListener(
      groupId = "${kafka.consumer.auto.group.id}",
      topics = "${kafka.names.topic.streams.optionChainData}")
  public void processChainData(KafkaOptionChainData message) {
    if (message.getLastTradeDate().equals(lastTradeDateBuilder.getDateStringFromToday())
        && message.getSymbol().equals(Symbol.SPX)
        && message.getCalls().size() > MININUM_CHAIN_SIZE
        && message.getPuts().size() > MININUM_CHAIN_SIZE) {
      ContractDataDBO contractDataDBO =
          chainDataContractDataCreateService.createIronCondorContractData(message);
      autoTradeMarketDataService.requestLiveMarketDataForContractData(
          Integer.parseInt(contractDataDBO.getLastTradeDate()), contractDataDBO);
      log.info("Requested MarketData for: " + contractDataDBO.getComboLegsDescription());
      autoTradeChainDataStopLiveDataService.stopMarketData(message);
    }
  }
}
