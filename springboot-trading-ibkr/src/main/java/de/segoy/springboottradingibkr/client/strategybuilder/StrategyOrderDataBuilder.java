package de.segoy.springboottradingibkr.client.strategybuilder;

import de.segoy.springboottradingdata.model.data.StrategyOrderData;
import de.segoy.springboottradingdata.model.data.entity.OrderData;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StrategyOrderDataBuilder {

    private final StrategyBuilderService strategyBuilderService;

    public StrategyOrderDataBuilder(StrategyBuilderService strategyBuilderService) {
        this.strategyBuilderService = strategyBuilderService;
    }

    public Optional<OrderData> buildOrderWithStrategyData(StrategyOrderData strategyOrderData) {
        return strategyBuilderService.getComboLegContractData(strategyOrderData.getOrderData().getContractData(),
                strategyOrderData.getStrategyLegs()).map(contractData -> {
            OrderData orderData = strategyOrderData.getOrderData();
            orderData.setContractData(contractData);
            return orderData;});
    }
}
