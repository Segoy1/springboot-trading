package de.segoy.springboottradingweb.spxautotrade.service;

import com.ib.client.Types;
import de.segoy.springboottradingdata.dataobject.ContractDataTemplates;
import de.segoy.springboottradingdata.model.Leg;
import de.segoy.springboottradingdata.model.data.StrategyContractData;
import de.segoy.springboottradingdata.model.data.entity.ContractDataDBO;
import de.segoy.springboottradingdata.model.data.kafka.KafkaOptionChainData;
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
  public ContractDataDBO createIronCondorContractData(KafkaOptionChainData kafkaOptionChainData) {
    List<Leg> legs = new ArrayList<>();
    double callShortStrike =
        kafkaOptionChainData
            .getCalls()
            .findClosestToDelta(tradeRuleSettingsConfig.getDelta())
            .getKey();
    legs.add(createLeg(Types.Right.Call, Types.Action.SELL, callShortStrike));

    double callLongStrike = callShortStrike + tradeRuleSettingsConfig.getSpreadSize();
    legs.add(createLeg(Types.Right.Call, Types.Action.BUY, callLongStrike));

    double putShortStrike =
        kafkaOptionChainData
            .getPuts()
            .findClosestToDelta(tradeRuleSettingsConfig.getDelta())
            .getKey();
    legs.add(createLeg(Types.Right.Put, Types.Action.SELL, putShortStrike));

    double putLongStrike = putShortStrike - tradeRuleSettingsConfig.getSpreadSize();
    legs.add(createLeg(Types.Right.Put, Types.Action.BUY, putLongStrike));

    ContractDataDBO contract = ContractDataTemplates.SPXWComboData();
    contract.setLastTradeDate(String.valueOf(kafkaOptionChainData.getLastTradeDate()));
    StrategyContractData strategyContractData =
        StrategyContractData.builder().contractDataDBO(contract).strategyLegs(legs).build();
    return strategyBuilderService.getComboLegContractData(strategyContractData).orElseThrow();
  }

  private Leg createLeg(Types.Right put, Types.Action buy, double putLongStrike) {
    return Leg.builder().right(put).action(buy).strike(putLongStrike).ratio(1).build();
  }
}
