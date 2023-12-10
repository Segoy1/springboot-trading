package de.segoy.springboottradingibkr.client.service.order;

import de.segoy.springboottradingdata.config.PropertiesConfig;
import de.segoy.springboottradingdata.model.OrderData;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrderService {

    private final OrderValidateAndPlacementService orderValidateAndPlacementService;
    private final PropertiesConfig propertiesConfig;


    public OrderService(OrderValidateAndPlacementService orderValidateAndPlacementService, PropertiesConfig propertiesConfig) {
        this.orderValidateAndPlacementService = orderValidateAndPlacementService;
        this.propertiesConfig = propertiesConfig;
    }

    public Optional<OrderData> setIdAndPlaceOrder(OrderData orderData) {
        if (orderData.getId() == null) {
            orderData.setId(propertiesConfig.getNextValidOrderId());
        }
        return orderValidateAndPlacementService.validateAndPlaceOrder(orderData);
    }
}
