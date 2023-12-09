package de.segoy.springboottradingibkr.client.service.order;

import de.segoy.springboottradingdata.model.OrderData;
import de.segoy.springboottradingdata.service.apiresponsecheck.OrderDataApiResponseChecker;
import de.segoy.springboottradingdata.config.PropertiesConfig;
import de.segoy.springboottradingibkr.client.service.contract.ContractDataValidator;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrderService {

    private final ContractDataValidator contractDataValidator;
    private final OrderPlacementService orderPlacementService;
    private final OrderDataApiResponseChecker orderDataApiResonseChecker;
    private final PropertiesConfig propertiesConfig;

    public OrderService( ContractDataValidator contractDataValidator, OrderPlacementService orderPlacementService, PropertiesConfig propertiesConfig, OrderDataApiResponseChecker orderDataApiResonseChecker) {
        this.contractDataValidator = contractDataValidator;
        this.orderPlacementService = orderPlacementService;
        this.orderDataApiResonseChecker = orderDataApiResonseChecker;
        this.propertiesConfig = propertiesConfig;
    }


    public Optional<OrderData> validateAndPlaceOrder(OrderData orderData) {
        if (orderData.getId() == null) {
            orderData.setId(propertiesConfig.getNextValidOrderId());
        }
        if (contractDataValidator.validate(orderData)) {
            orderPlacementService.placeOrder(orderData);
            return orderDataApiResonseChecker.checkForApiResponseAndUpdate(orderData.getId());
        }
        return Optional.empty();
    }
}
