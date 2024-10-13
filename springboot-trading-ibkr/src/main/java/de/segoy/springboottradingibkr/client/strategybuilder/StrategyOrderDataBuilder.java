package de.segoy.springboottradingibkr.client.strategybuilder;

import de.segoy.springboottradingdata.model.data.StrategyContractData;
import de.segoy.springboottradingdata.model.data.StrategyOrderData;
import de.segoy.springboottradingdata.model.data.entity.OrderDbo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StrategyOrderDataBuilder {

  private final StrategyBuilderService strategyBuilderService;

  public Optional<OrderDbo> buildOrderWithStrategyData(StrategyOrderData strategyOrderData) {
    return strategyBuilderService
        .getComboLegContractData(
            StrategyContractData.builder()
                .contractDBO(strategyOrderData.getOrderData().getContractDBO())
                .strategyLegs(strategyOrderData.getStrategyLegs())
                .build())
        .map(
            contractData -> {
              OrderDbo orderData = strategyOrderData.getOrderData();
              orderData.setContractDBO(contractData);
              return orderData;
            });
  }
}
