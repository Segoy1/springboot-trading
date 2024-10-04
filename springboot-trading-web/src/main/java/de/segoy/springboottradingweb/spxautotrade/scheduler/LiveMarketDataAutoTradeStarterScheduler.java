package de.segoy.springboottradingweb.spxautotrade.scheduler;

import de.segoy.springboottradingdata.config.PropertiesConfig;
import de.segoy.springboottradingdata.model.data.entity.LastPriceLiveMarketData;
import de.segoy.springboottradingdata.repository.LastPriceLiveMarketDataRepository;
import de.segoy.springboottradingweb.spxautotrade.service.AutoTradeCallAndPutDataRequestService;
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

  @Scheduled(cron = "0 30 15 * * 1-5")
  //  @Scheduled(cron = "*/30 * * * * *")
  public void getOptionDataForDayTradeStrategy() {
    LastPriceLiveMarketData liveData =
        lastPriceLiveMarketDataRepository
            .findById((long) propertiesConfig.getSpxTickerId())
            .orElseThrow(() -> new RuntimeException("No Live Data for SPX found"));
    autoTradeOptionDataService.getOptionContractsAndCallAPI(liveData.getLastPrice());
  }
}
