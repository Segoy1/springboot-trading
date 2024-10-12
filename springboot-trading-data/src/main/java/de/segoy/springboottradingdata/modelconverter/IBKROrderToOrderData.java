package de.segoy.springboottradingdata.modelconverter;

import com.ib.client.Order;
import de.segoy.springboottradingdata.model.data.entity.OrderDataDBO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class IBKROrderToOrderData {

    public OrderDataDBO convertOrder(Order order) {
        OrderDataDBO orderData = OrderDataDBO.builder()
                .id((long) order.orderId())
                .action(order.action())
                .totalQuantity(order.totalQuantity().value().stripTrailingZeros())
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
