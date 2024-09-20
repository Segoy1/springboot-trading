package de.segoy.springboottradingweb.spxautotrade.service;

import com.ib.client.Types;
import de.segoy.springboottradingdata.model.Leg;
import de.segoy.springboottradingdata.model.data.OptionChainData;
import de.segoy.springboottradingdata.model.data.StrategyContractData;
import de.segoy.springboottradingdata.model.data.entity.ContractData;
import de.segoy.springboottradingibkr.client.strategybuilder.StrategyBuilderService;
import de.segoy.springboottradingweb.spxautotrade.settings.TradeRuleSettingsConfig;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChainDataContractDataCreateService {

  private final TradeRuleSettingsConfig tradeRuleSettingsConfig;
  private final StrategyBuilderService strategyBuilderService;

  @Transactional
  public ContractData createIronCondorContractData(OptionChainData optionChainData) {
    List<Leg> legs = new ArrayList<>();
    double callShortStrike =
        optionChainData.getCalls().findClosestToDelta(tradeRuleSettingsConfig.getDelta()).getKey();
    legs.add(createLeg(Types.Right.Call, Types.Action.SELL, callShortStrike));

    double callLongStrike = callShortStrike + tradeRuleSettingsConfig.getSpreadSize();
    legs.add(createLeg(Types.Right.Call, Types.Action.BUY, callLongStrike));

    double putShortStrike =
        optionChainData.getPuts().findClosestToDelta(tradeRuleSettingsConfig.getDelta()).getKey();
    legs.add(createLeg(Types.Right.Put, Types.Action.SELL, putShortStrike));

    double putLongStrike = putShortStrike - tradeRuleSettingsConfig.getSpreadSize();
    legs.add(createLeg(Types.Right.Put, Types.Action.BUY, putLongStrike));

    ContractData contract =
        ContractData.builder()
            .lastTradeDate(optionChainData.getLastTradeDate())
            .symbol(optionChainData.getSymbol())
            .securityType(Types.SecType.BAG)
            .build();
    StrategyContractData strategyContractData =
        StrategyContractData.builder().contractData(contract).strategyLegs(legs).build();
    return strategyBuilderService.getComboLegContractData(strategyContractData).orElseThrow();
  }

  private Leg createLeg(Types.Right put, Types.Action buy, double putLongStrike) {
    return
            Leg.builder()
                    .right(put)
                    .action(buy)
                    .strike(putLongStrike)
                    .ratio(1)
                    .build();
  }

}
