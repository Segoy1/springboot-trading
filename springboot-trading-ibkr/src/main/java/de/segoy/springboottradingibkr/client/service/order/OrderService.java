package de.segoy.springboottradingibkr.client.service.order;

import de.segoy.springboottradingdata.config.PropertiesConfig;
import de.segoy.springboottradingdata.model.entity.OrderData;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final OrderValidateAndPlacementService orderValidateAndPlacementService;
    private final PropertiesConfig propertiesConfig;


    public OrderService(OrderValidateAndPlacementService orderValidateAndPlacementService, PropertiesConfig propertiesConfig) {
        this.orderValidateAndPlacementService = orderValidateAndPlacementService;
        this.propertiesConfig = propertiesConfig;
    }

    public void setIdAndPlaceOrder(OrderData orderData) {
        if (orderData.getId() == null) {
            orderData.setId(propertiesConfig.getNextValidOrderId());
        }
        orderValidateAndPlacementService.validateAndPlaceOrder(orderData);
    }
}
