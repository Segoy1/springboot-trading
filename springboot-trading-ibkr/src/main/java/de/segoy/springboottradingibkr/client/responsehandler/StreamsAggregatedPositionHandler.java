package de.segoy.springboottradingibkr.client.responsehandler;

import de.segoy.springboottradingdata.config.TradeRuleSettingsConfig;
import de.segoy.springboottradingdata.model.data.entity.ContractDbo;
import de.segoy.springboottradingdata.model.data.entity.PositionDbo;
import de.segoy.springboottradingdata.model.data.kafka.PositionData;
import de.segoy.springboottradingdata.modelconverter.PositionDataToDbo;
import de.segoy.springboottradingdata.modelsynchronize.PositionDataDatabaseSynchronizer;
import de.segoy.springboottradingdata.optionstradingservice.AutotradeDbAndTickerIdEncoder;
import de.segoy.springboottradingdata.optionstradingservice.LastTradeDateBuilder;
import de.segoy.springboottradingdata.service.StrategyNameService;
import de.segoy.springboottradingibkr.client.service.contract.UniqueContractDataProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class StreamsAggregatedPositionHandler {

  private final UniqueContractDataProvider uniqueContractDataProvider;
  private final PositionDataDatabaseSynchronizer positionDataDatabaseSynchronizer;
  private final PositionDataToDbo positionDataToDbo;
  private final LastTradeDateBuilder lastTradeDateBuilder;
  private final AutotradeDbAndTickerIdEncoder autotradeDbAndTickerIdEncoder;
  private final TradeRuleSettingsConfig tradeRuleSettingsConfig;
  private final StrategyNameService strategyNameService;

  public Optional<PositionData> persistContractAndPositionData(PositionData positionData) {
    PositionDbo positionDbo = positionDataToDbo.convert(positionData);
    ContractDbo persistedContract =
        uniqueContractDataProvider
            .getExistingContractDataOrCallApi(positionDbo.getContractDBO())
            .orElseThrow();
    positionDbo.setContractDBO(persistedContract);
    setIdIfAutoTrade(persistedContract, positionDbo);
    return positionDataDatabaseSynchronizer
        .updateInDbOrSave(positionDbo)
        .map(PositionDbo::toKafkaPositionData)
        .or(Optional::empty);
  }

  /** if LastTradeDate is today and Symbol is SPX it is Auto Trade */
  private void setIdIfAutoTrade(ContractDbo contractDbo, PositionDbo positionDbo) {
    if (contractDbo.getLastTradeDate().equals(lastTradeDateBuilder.getDateStringFromToday())
        && contractDbo.getSymbol().equals(tradeRuleSettingsConfig.getTradeSymbol())) {
      positionDbo.setId(
          autotradeDbAndTickerIdEncoder.generateLongForTodayBySymbolAndStrategy(
              contractDbo.getSymbol(),
              strategyNameService.resolveStrategyFromComboLegs(contractDbo.getComboLegs())));
    }
  }
}
