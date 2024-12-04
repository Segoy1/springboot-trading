package de.segoy.springboottradingweb.spxautotrade.service;

import de.segoy.springboottradingdata.model.data.entity.ContractDbo;
import de.segoy.springboottradingdata.optionstradingservice.AutotradeDbAndTickerIdEncoder;
import de.segoy.springboottradingdata.repository.LastPriceLiveMarketDataRepository;
import de.segoy.springboottradingdata.service.RepositoryRefreshService;
import de.segoy.springboottradingdata.service.StrategyNameService;
import de.segoy.springboottradingibkr.client.service.marketdata.AutoTradeMarketDataService;
import de.segoy.springboottradingibkr.client.service.marketdata.StopMarketDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdatedStrategyMarketDataRequestService {
  private final AutoTradeMarketDataService autoTradeMarketDataService;
  private final StopMarketDataService stopMarketDataService;
  private final AutotradeDbAndTickerIdEncoder autotradeDbAndTickerIdEncoder;
  private final StrategyNameService strategyNameService;
  private final LastPriceLiveMarketDataRepository lastPriceLiveMarketDataRepository;
  private final RepositoryRefreshService repositoryRefreshService;

  public void stopOldAndRequestNewLiveData(ContractDbo contractDbo) {
    Integer tickerId =
        autotradeDbAndTickerIdEncoder.generateIntForLastTradeDateBySymbolAndStrategy(
            Long.valueOf(contractDbo.getLastTradeDate()),
            contractDbo.getSymbol(),
            strategyNameService.resolveStrategyFromComboLegs(contractDbo.getComboLegs()));
    if (tickerId != null) {
      stopMarketDataService.stopMarketDataForTickerId(tickerId);
      waitUntilOldDataStopped(tickerId);
      autoTradeMarketDataService.requestLiveMarketDataForContractData(tickerId, contractDbo);
    }
  }

  private void waitUntilOldDataStopped(long tickerId) {
    lastPriceLiveMarketDataRepository.deleteById(tickerId);
  }
}
