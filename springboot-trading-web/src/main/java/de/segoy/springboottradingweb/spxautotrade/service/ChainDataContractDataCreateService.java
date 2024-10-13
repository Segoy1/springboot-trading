package de.segoy.springboottradingweb.spxautotrade.service;

import com.ib.client.Types;
import de.segoy.springboottradingdata.dataobject.ContractDataTemplates;
import de.segoy.springboottradingdata.model.Leg;
import de.segoy.springboottradingdata.model.data.StrategyContractData;
import de.segoy.springboottradingdata.model.data.entity.ContractDbo;
import de.segoy.springboottradingdata.model.data.kafka.OptionChainData;
import de.segoy.springboottradingibkr.client.strategybuilder.StrategyBuilderService;
import de.segoy.springboottradingweb.spxautotrade.settings.TradeRuleSettingsConfig;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * This Class is responsible for applying the rules by which the strategy is built. With the
 * Parameters parsed in at programm start
 */
@Service
@RequiredArgsConstructor
public class ChainDataContractDataCreateService {

  private final TradeRuleSettingsConfig tradeRuleSettingsConfig;
  private final StrategyBuilderService strategyBuilderService;

  @Transactional
  public ContractDbo createIronCondorContractData(OptionChainData optionChainData) {
    List<Leg> legs = new ArrayList<>();
    double callShortStrike =
        optionChainData
            .getCalls()
            .findClosestToDelta(tradeRuleSettingsConfig.getDelta())
            .getKey();
    legs.add(createLeg(Types.Right.Call, Types.Action.SELL, callShortStrike));

    double callLongStrike = callShortStrike + tradeRuleSettingsConfig.getSpreadSize();
    legs.add(createLeg(Types.Right.Call, Types.Action.BUY, callLongStrike));

    double putShortStrike =
        optionChainData
            .getPuts()
            .findClosestToDelta(tradeRuleSettingsConfig.getDelta())
            .getKey();
    legs.add(createLeg(Types.Right.Put, Types.Action.SELL, putShortStrike));

    double putLongStrike = putShortStrike - tradeRuleSettingsConfig.getSpreadSize();
    legs.add(createLeg(Types.Right.Put, Types.Action.BUY, putLongStrike));

    ContractDbo contract = ContractDataTemplates.SPXWComboData();
    contract.setLastTradeDate(String.valueOf(optionChainData.getLastTradeDate()));
    StrategyContractData strategyContractData =
        StrategyContractData.builder().contractDBO(contract).strategyLegs(legs).build();
    return strategyBuilderService.getComboLegContractData(strategyContractData).orElseThrow();
  }

  private Leg createLeg(Types.Right put, Types.Action buy, double putLongStrike) {
    return Leg.builder().right(put).action(buy).strike(putLongStrike).ratio(1).build();
  }
}
