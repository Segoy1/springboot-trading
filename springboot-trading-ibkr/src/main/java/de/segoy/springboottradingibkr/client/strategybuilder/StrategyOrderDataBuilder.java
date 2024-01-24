package de.segoy.springboottradingibkr.client.strategybuilder;

import de.segoy.springboottradingdata.model.data.StrategyData;
import de.segoy.springboottradingdata.model.data.entity.OrderData;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StrategyOrderDataBuilder {

    private final StrategyBuilderService strategyBuilderService;

    public StrategyOrderDataBuilder(StrategyBuilderService strategyBuilderService) {
        this.strategyBuilderService = strategyBuilderService;
    }

    public Optional<OrderData> buildOrderWithStrategyData(StrategyData strategyData) {
        return strategyBuilderService.getComboLegContractData(strategyData.getOrderData().getContractData(),
                strategyData.getStrategyLegs()).map(contractData -> {
            OrderData orderData = strategyData.getOrderData();
            orderData.setContractData(contractData);
            return orderData;});
    }
}
