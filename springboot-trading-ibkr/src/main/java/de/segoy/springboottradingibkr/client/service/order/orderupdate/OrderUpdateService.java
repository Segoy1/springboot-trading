package de.segoy.springboottradingibkr.client.service.order.orderupdate;

import de.segoy.springboottradingdata.model.data.entity.OrderData;
import de.segoy.springboottradingdata.repository.OrderDataRepository;
import de.segoy.springboottradingibkr.client.service.ApiCaller;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class OrderUpdateService {

    private final OrderDataRepository orderDataRepository;
    private final @Qualifier("OrderPlacementApiCaller") ApiCaller<OrderData> orderPlacementApiCaller;

    public void updateOrderLimitPrice(Long id, BigDecimal price) {
        callApiAndGetResponse(orderDataRepository.findById(id).map((orderData) -> {
            orderData.setLimitPrice(price);
            return orderData;
        }).orElseThrow());
    }

    public void updateOrderQuantity(Long id, BigDecimal quantity) {
        callApiAndGetResponse(orderDataRepository.findById(id).map((orderData) -> {
            orderData.setTotalQuantity(quantity);
            return orderData;
        }).orElseThrow());
    }


    private void callApiAndGetResponse(OrderData updatedOrderData) {
        orderPlacementApiCaller.callApi(updatedOrderData);
    }
}
