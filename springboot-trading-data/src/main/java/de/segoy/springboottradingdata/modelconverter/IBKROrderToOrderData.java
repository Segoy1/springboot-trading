package de.segoy.springboottradingdata.modelconverter;

import com.ib.client.Order;
import de.segoy.springboottradingdata.model.entity.OrderData;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class IBKROrderToOrderData {

    public OrderData convertOrder(Order order) {
        OrderData orderData = OrderData.builder()
                .id((long) order.orderId())
                .action(order.action())
                .totalQuantity(order.totalQuantity().value())
                .orderType(order.orderType())
                .timeInForce(order.tif())
                .limitPrice(BigDecimal.valueOf(order.lmtPrice()))
                .auctionPrice(BigDecimal.valueOf(order.auxPrice()))
                .cashQuantity(BigDecimal.valueOf(order.cashQty()))
                .usePriceManagementAlgorithm(order.usePriceMgmtAlgo())
                .build();

        //Builder from Super Class not inherited
//        orderData.setTouchedByApi(true);
        return orderData;
    }
}
