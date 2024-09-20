package de.segoy.springboottradingibkr.client.strategybuilder;

import de.segoy.springboottradingdata.model.data.StrategyContractData;
import de.segoy.springboottradingdata.model.data.StrategyOrderData;
import de.segoy.springboottradingdata.model.data.entity.OrderData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StrategyOrderDataBuilder {

  private final StrategyBuilderService strategyBuilderService;

  public Optional<OrderData> buildOrderWithStrategyData(StrategyOrderData strategyOrderData) {
    return strategyBuilderService
        .getComboLegContractData(
            StrategyContractData.builder()
                .contractData(strategyOrderData.getOrderData().getContractData())
                .strategyLegs(strategyOrderData.getStrategyLegs())
                .build())
        .map(
            contractData -> {
              OrderData orderData = strategyOrderData.getOrderData();
              orderData.setContractData(contractData);
              return orderData;
            });
  }
}
