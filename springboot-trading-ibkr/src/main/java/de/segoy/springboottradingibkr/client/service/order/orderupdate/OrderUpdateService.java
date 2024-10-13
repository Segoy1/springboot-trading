package de.segoy.springboottradingibkr.client.service.order.orderupdate;

import de.segoy.springboottradingdata.model.data.entity.OrderDbo;
import de.segoy.springboottradingdata.repository.OrderRepository;
import de.segoy.springboottradingibkr.client.service.ApiCaller;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class OrderUpdateService {

    private final OrderRepository orderRepository;
    private final @Qualifier("OrderPlacementApiCaller") ApiCaller<OrderDbo> orderPlacementApiCaller;

    public void updateOrderLimitPrice(Long id, BigDecimal price) {
        callApiAndGetResponse(orderRepository.findById(id).map((orderData) -> {
            orderData.setLimitPrice(price);
            return orderData;
        }).orElseThrow());
    }

    public void updateOrderQuantity(Long id, BigDecimal quantity) {
        callApiAndGetResponse(orderRepository.findById(id).map((orderData) -> {
            orderData.setTotalQuantity(quantity);
            return orderData;
        }).orElseThrow());
    }


    private void callApiAndGetResponse(OrderDbo updatedOrderData) {
        orderPlacementApiCaller.callApi(updatedOrderData);
    }
}
