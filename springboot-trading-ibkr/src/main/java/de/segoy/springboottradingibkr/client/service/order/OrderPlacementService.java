package de.segoy.springboottradingibkr.client.service.order;

import de.segoy.springboottradingdata.config.PropertiesConfig;
import de.segoy.springboottradingdata.model.data.entity.OrderDbo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderPlacementService {

    private final OrderValidateAndPlacementService orderValidateAndPlacementService;
    private final PropertiesConfig propertiesConfig;

    public void setIdAndPlaceOrder(OrderDbo orderData) {
        if (orderData.getId() == null) {
            orderData.setId(propertiesConfig.getNextValidOrderId());
        }
        orderValidateAndPlacementService.validateAndPlaceOrder(orderData);
    }
}
