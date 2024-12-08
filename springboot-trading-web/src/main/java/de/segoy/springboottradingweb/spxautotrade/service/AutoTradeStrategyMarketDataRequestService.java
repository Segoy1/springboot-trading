package de.segoy.springboottradingweb.spxautotrade.service;

import de.segoy.springboottradingdata.config.TradeRuleSettingsConfig;
import de.segoy.springboottradingdata.model.data.entity.ContractDbo;
import de.segoy.springboottradingdata.model.data.entity.OptionChainDbo;
import de.segoy.springboottradingdata.model.data.kafka.OptionChainData;
import de.segoy.springboottradingdata.modelconverter.DboToOptionChainData;
import de.segoy.springboottradingdata.optionstradingservice.AutoTradeIdService;
import de.segoy.springboottradingdata.optionstradingservice.AutotradeDbAndTickerIdEncoder;
import de.segoy.springboottradingdata.repository.OptionChainRepository;
import de.segoy.springboottradingdata.service.RepositoryRefreshService;
import de.segoy.springboottradingibkr.client.service.marketdata.AutoTradeMarketDataService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AutoTradeStrategyMarketDataRequestService {

  private final TradeRuleSettingsConfig tradeRuleSettingsConfig;
  private final StrategyFromChainDataCreator strategyFromChainDataCreator;
  private final AutoTradeMarketDataService autoTradeMarketDataService;
  private final AutoTradeChainDataStopLiveDataService autoTradeChainDataStopLiveDataService;
  private final OptionChainRepository optionChainRepository;
  private final DboToOptionChainData dboToOptionChainData;
  private final RepositoryRefreshService repositoryRefreshService;
  private final AutotradeDbAndTickerIdEncoder autotradeDbAndTickerIdEncoder;
  private final AutoTradeIdService autoTradeIdService;

  @Transactional
  public ContractDbo createStrategyFromOptionChain() {
    OptionChainData chainData = dboToOptionChainData.toOptionChainData(findFromRepo());

    ContractDbo contractDBO = strategyFromChainDataCreator.createIronCondorContractData(chainData);
    autoTradeMarketDataService.requestLiveMarketDataForContractData(
        autoTradeIdService.setIdForAutoTrade(contractDBO).intValue(), contractDBO);
    log.info("Requested MarketData for: " + contractDBO.getComboLegsDescription());
    autoTradeChainDataStopLiveDataService.stopMarketData(chainData);
    return contractDBO;
  }

  private OptionChainDbo findFromRepo() {
    return optionChainRepository
        .findById(
            autotradeDbAndTickerIdEncoder.generateLongIdIdForTodayBySymbol(
                tradeRuleSettingsConfig.getTradeSymbol()))
        .orElseGet(
            () -> {
              repositoryRefreshService.clearCacheAndWait(optionChainRepository);
              return findFromRepo();
            });
  }
}
