package de.segoy.springboottradingweb.spxautotrade.scheduler;

import de.segoy.springboottradingdata.config.PropertiesConfig;
import de.segoy.springboottradingdata.dataobject.ContractDataTemplates;
import de.segoy.springboottradingdata.model.data.entity.ContractDbo;
import de.segoy.springboottradingdata.model.data.entity.LastPriceLiveMarketDataDbo;
import de.segoy.springboottradingdata.repository.LastPriceLiveMarketDataRepository;
import de.segoy.springboottradingdata.service.RepositoryRefreshService;
import de.segoy.springboottradingdata.service.StrategyNameService;
import de.segoy.springboottradingibkr.client.service.tradinghours.TradingHoursService;
import de.segoy.springboottradingweb.spxautotrade.service.AutoTradeCallAndPutDataRequestService;
import de.segoy.springboottradingweb.spxautotrade.service.AutoTradeStaleOptionChainDataClearService;
import de.segoy.springboottradingweb.spxautotrade.service.AutoTradeStrategyMarketDataRequestService;
import de.segoy.springboottradingweb.spxautotrade.service.SpxLiveDataActivator;
import de.segoy.springboottradingweb.spxautotrade.service.order.OrderSubmitAutoTradeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Slf4j
@Component
public class LiveMarketDataAutoTradeStarterScheduler {

  private final AutoTradeCallAndPutDataRequestService autoTradeOptionDataService;
  private final LastPriceLiveMarketDataRepository lastPriceLiveMarketDataRepository;
  private final PropertiesConfig propertiesConfig;
  private final SpxLiveDataActivator spxLiveDataActivator;
  private final RepositoryRefreshService repositoryRefreshService;
  private final AutoTradeStrategyMarketDataRequestService autoTradeStrategyMarketDataRequestService;
  private final OrderSubmitAutoTradeService orderSubmitAutoTradeService;
  private final StrategyNameService strategyNameService;
  private final TradingHoursService tradingHoursService;
  private final AutoTradeStaleOptionChainDataClearService autoTradeStaleOptionChainDataClearService;

  @Scheduled(cron = "5 30 15 * * 1-5")
  public void getOptionDataForDayTradeStrategy() {
    if (tradingHoursService.isOpenToday(ContractDataTemplates.SpxData())) {

      autoTradeStaleOptionChainDataClearService.clearStaleOptionMarketData();

      LastPriceLiveMarketDataDbo liveData = getLiveData(true);
      autoTradeOptionDataService.getOptionContractsAndCallAPI(liveData.getLastPrice());

      ContractDbo strategyContract =
          autoTradeStrategyMarketDataRequestService.createStrategyFromOptionChain();

      orderSubmitAutoTradeService.placeOrderAndIfNecessaryUpdateStrategy(
          strategyNameService.resolveStrategyFromComboLegs(strategyContract.getComboLegs()));
      log.info("Strategy successfully ordered: {}", strategyContract.getComboLegsDescription());
    }
  }

  private LastPriceLiveMarketDataDbo getLiveData(boolean isFirst) {
    LastPriceLiveMarketDataDbo liveData =
        lastPriceLiveMarketDataRepository
            .findById((long) propertiesConfig.getSpxTickerId())
            .orElseGet(() -> repeatWithRefresh(isFirst));
    if(liveData.getLastModifiedDate().isBefore(propertiesConfig.getFiveSecondsAgo())){
      liveData = repeatWithRefresh(isFirst);
    }
    return liveData.getLastPrice() != null ? liveData : repeatWithRefresh(false);
  }

  private LastPriceLiveMarketDataDbo repeatWithRefresh(boolean isFirst) {
    if (isFirst) {
      spxLiveDataActivator.getLiveMarketDataSPX();
    }
    repositoryRefreshService.clearCacheAndWait(lastPriceLiveMarketDataRepository);
    return getLiveData(false);
  }
}
