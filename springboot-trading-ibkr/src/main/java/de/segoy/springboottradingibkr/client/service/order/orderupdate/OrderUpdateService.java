package de.segoy.springboottradingibkr.client.service.order.orderupdate;

import de.segoy.springboottradingdata.model.OrderData;
import de.segoy.springboottradingdata.repository.OrderDataRepository;
import de.segoy.springboottradingdata.service.apiresponsecheck.ApiResponseCheckerForOptional;
import de.segoy.springboottradingibkr.client.service.ApiCaller;
import org.springframework.beans.factory.annotation.Qualifier;

import java.math.BigDecimal;
import java.util.Optional;

public class OrderUpdateService {

    private final OrderDataRepository orderDataRepository;
    private final ApiResponseCheckerForOptional<OrderData> orderDataApiResponseChecker;
    private final ApiCaller<OrderData> orderPlacementApiCaller;

    public OrderUpdateService(OrderDataRepository orderDataRepository,
                              ApiResponseCheckerForOptional<OrderData> orderDataApiResponseChecker,
                              @Qualifier("OrderPlacementApiCaller") ApiCaller<OrderData> orderPlacementApiCaller) {
        this.orderDataRepository = orderDataRepository;
        this.orderDataApiResponseChecker = orderDataApiResponseChecker;
        this.orderPlacementApiCaller = orderPlacementApiCaller;
    }

    public Optional<OrderData> updateOrderLimitPrice(Long id, BigDecimal price) {
        return callApiAndGetResponse(orderDataRepository.findById(id).map((orderData) -> {
            orderData.setLimitPrice(price);
            return orderData;
        }).orElseThrow());
    }

    public Optional<OrderData> updateOrderQuantity(Long id, BigDecimal quantity) {
        return callApiAndGetResponse(orderDataRepository.findById(id).map((orderData) -> {
            orderData.setTotalQuantity(quantity);
            return orderData;
        }).orElseThrow());
    }


    private Optional<OrderData> callApiAndGetResponse(OrderData updatedOrderData) {
        orderPlacementApiCaller.callApi(updatedOrderData);
        return orderDataApiResponseChecker.checkForApiResponseAndUpdate(updatedOrderData.getId().intValue());
    }
}
