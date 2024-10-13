package de.segoy.springboottradingweb.spxautotrade.scheduler;

import de.segoy.springboottradingdata.model.data.entity.ContractDbo;
import de.segoy.springboottradingdata.model.data.entity.OptionChainDbo;
import de.segoy.springboottradingdata.model.data.kafka.KafkaOptionChainData;
import de.segoy.springboottradingdata.modelconverter.DboToOptionChainData;
import de.segoy.springboottradingdata.optionstradingservice.LastTradeDateBuilder;
import de.segoy.springboottradingdata.repository.OptionChainRepository;
import de.segoy.springboottradingibkr.client.service.marketdata.AutoTradeMarketDataService;
import de.segoy.springboottradingweb.spxautotrade.service.AutoTradeChainDataStopLiveDataService;
import de.segoy.springboottradingweb.spxautotrade.service.ChainDataContractDataCreateService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AutoTradeStrategyMarketDataRequestService {

  private final LastTradeDateBuilder lastTradeDateBuilder;
  private final ChainDataContractDataCreateService chainDataContractDataCreateService;
  private final AutoTradeMarketDataService autoTradeMarketDataService;
  private final AutoTradeChainDataStopLiveDataService autoTradeChainDataStopLiveDataService;
  private final OptionChainRepository optionChainRepository;
  private final DboToOptionChainData dboToOptionChainData;


  @Transactional
  public void createStrategyFromOptionChain() {
    KafkaOptionChainData chainData = dboToOptionChainData.toOptionChainData(findFromRepo());

    ContractDbo contractDBO =
        chainDataContractDataCreateService.createIronCondorContractData(chainData);
    autoTradeMarketDataService.requestLiveMarketDataForContractData(
        Integer.parseInt(contractDBO.getLastTradeDate()), contractDBO);
    log.info("Requested MarketData for: " + contractDBO.getComboLegsDescription());
    autoTradeChainDataStopLiveDataService.stopMarketData(chainData);
  }

  private OptionChainDbo findFromRepo() {
    return optionChainRepository
        .findById(lastTradeDateBuilder.getDateLongFromToday())
        .orElseGet(
            () -> {
              try {
                Thread.sleep(20L);
              } catch (InterruptedException e) {
                log.error("Thread.sleep() failed", e);
              }
              return findFromRepo();
            });
  }
}
