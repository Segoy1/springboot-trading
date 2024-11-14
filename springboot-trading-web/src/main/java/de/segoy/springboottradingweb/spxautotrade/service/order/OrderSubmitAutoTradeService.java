package de.segoy.springboottradingweb.spxautotrade.service.order;

import de.segoy.springboottradingdata.config.TradeRuleSettingsConfig;
import de.segoy.springboottradingdata.model.data.entity.LastPriceLiveMarketDataDbo;
import de.segoy.springboottradingdata.model.data.entity.OrderDbo;
import de.segoy.springboottradingdata.model.subtype.Strategy;
import de.segoy.springboottradingdata.optionstradingservice.AutotradeDbAndTickerIdEncoder;
import de.segoy.springboottradingdata.repository.LastPriceLiveMarketDataRepository;
import de.segoy.springboottradingdata.service.RepositoryRefreshService;
import de.segoy.springboottradingibkr.client.service.order.OrderPlacementService;
import de.segoy.springboottradingweb.spxautotrade.service.StrategyStrikesUpdateService;
import de.segoy.springboottradingweb.spxautotrade.service.UpdatedStrategyMarketDataRequestService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderSubmitAutoTradeService {
  private final LastPriceLiveMarketDataRepository lastPriceLiveMarketDataRepository;
  private final AutotradeDbAndTickerIdEncoder autotradeDbAndTickerIdEncoder;
  private final RepositoryRefreshService repositoryRefreshService;
  private final OrderPlacementService orderPlacementService;
  private final OrderCreateAutoTradeService orderCreateAutoTradeService;
  private final TradeRuleSettingsConfig tradeRuleSettingsConfig;
  private final StrategyStrikesUpdateService strategyStrikesUpdateService;
  private final UpdatedStrategyMarketDataRequestService updatedStrategyMarketDataRequestService;

  @Transactional
  public void placeOrderAndIfNecessaryUpdateStrategy(Strategy strategy) {
    final long id =
        autotradeDbAndTickerIdEncoder.generateLongForTodayBySymbolAndStrategy(
            tradeRuleSettingsConfig.getTradeSymbol(), strategy);
    OrderDbo order = createOrderAndIfNecessaryUpdateStrategy(id, strategy);
    orderPlacementService.placeOrderWithAutoIdIfNotSet(order);
  }

  private OrderDbo createOrderAndIfNecessaryUpdateStrategy(long id, Strategy strategy) {
    LastPriceLiveMarketDataDbo liveData = getLiveDataOrRefresh(id);
    final double limitMinusTolerance =
        tradeRuleSettingsConfig.getLimitValue()
            - tradeRuleSettingsConfig.getToleranceForOrderFill();

    if (Math.abs(liveData.getBidPrice()) <= limitMinusTolerance) {
      return orderCreateAutoTradeService.setupOrderWithLmtPriceHalfwayBetweenBidAndAsk(
          liveData, strategy);
    } else {
      // build new Strategy and get live Data from it then make order
      updatedStrategyMarketDataRequestService.stopOldAndRequestNewLiveData(
          strategyStrikesUpdateService.updateStrategyStrikes(liveData.getContractDBO()));
      return createOrderAndIfNecessaryUpdateStrategy(id, strategy);
    }
  }

  private LastPriceLiveMarketDataDbo getLiveDataOrRefresh(long id) {
    LastPriceLiveMarketDataDbo liveData =
        lastPriceLiveMarketDataRepository
            .findById(id)
            .orElseGet(
                () -> {
                  repositoryRefreshService.clearCacheAndWait(lastPriceLiveMarketDataRepository);
                  return getLiveDataOrRefresh(id);
                });
    // Ensure Bid Price is not null
    if (liveData.getBidPrice() == null || liveData.getAskPrice() == null) {
      repositoryRefreshService.clearCacheAndWait(lastPriceLiveMarketDataRepository);
      return getLiveDataOrRefresh(id);
    } else {
      return liveData;
    }
  }
}
