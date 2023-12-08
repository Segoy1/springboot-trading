package de.segoy.springboottradingdata.modelconverter;

import com.ib.client.Decimal;
import com.ib.client.Order;
import de.segoy.springboottradingdata.model.OrderData;
import org.springframework.stereotype.Component;

@Component
public class OrderDataToIBKROrder {

    public Order convertOrderData(OrderData orderData) {
        Order order = new Order();

        order.orderId(orderData.getId().intValue());
        order.action(orderData.getAction());
        order.totalQuantity(Decimal.get(orderData.getTotalQuantity()));
        order.orderType(orderData.getOrderType().toString());
        if(orderData.getTimeInForce() != null){
        order.tif(orderData.getTimeInForce());
        }
        if (orderData.getLimitPrice()!= null) {
        order.lmtPrice(orderData.getLimitPrice().doubleValue());
        }
        if(orderData.getAuctionPrice() != null) {
            order.auxPrice(orderData.getAuctionPrice().doubleValue());
        }
        if(orderData.getCashQuantity() != null) {
            order.cashQty(orderData.getCashQuantity().doubleValue());
        }
        order.usePriceMgmtAlgo(orderData.getUsePriceManagementAlgorithm());

        return order;
    }
}
