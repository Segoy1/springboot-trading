package de.segoy.springboottradingweb.spxautotrade.scheduler;

import de.segoy.springboottradingdata.config.PropertiesConfig;
import de.segoy.springboottradingdata.model.data.entity.LastPriceLiveMarketDataDbo;
import de.segoy.springboottradingdata.model.subtype.Strategy;
import de.segoy.springboottradingdata.repository.LastPriceLiveMarketDataRepository;
import de.segoy.springboottradingdata.service.RepositoryRefreshService;
import de.segoy.springboottradingweb.spxautotrade.service.AutoTradeCallAndPutDataRequestService;
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

  @Scheduled(cron = "0 30 15 * * 1-5")
  //  @Scheduled(cron = "*/30 * * * * *")
  public void getOptionDataForDayTradeStrategy() {
    LastPriceLiveMarketDataDbo liveData = getLiveData();
    autoTradeOptionDataService.getOptionContractsAndCallAPI(liveData.getLastPrice());
    Strategy strategy = Strategy.IRON_CONDOR;
    autoTradeStrategyMarketDataRequestService.createStrategyFromOptionChain(strategy);
    orderSubmitAutoTradeService.placeOrderAndIfNecessaryUpdateStrategy(strategy);
  }

  private LastPriceLiveMarketDataDbo getLiveData() {
    LastPriceLiveMarketDataDbo liveData =
        lastPriceLiveMarketDataRepository
            .findById((long) propertiesConfig.getSpxTickerId())
            .orElseGet(this::repeatWithRefresh);
    return liveData.getLastPrice() != null ? liveData : repeatWithRefresh();
  }

  private LastPriceLiveMarketDataDbo repeatWithRefresh() {
    spxLiveDataActivator.getLiveMarketDataSPX();
    repositoryRefreshService.clearCacheAndWait(lastPriceLiveMarketDataRepository);
    return getLiveData();
  }
}
