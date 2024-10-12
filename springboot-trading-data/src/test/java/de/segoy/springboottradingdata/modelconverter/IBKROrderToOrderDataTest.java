package de.segoy.springboottradingdata.modelconverter;

import com.ib.client.Decimal;
import com.ib.client.Order;
import com.ib.client.OrderType;
import com.ib.client.Types;
import de.segoy.springboottradingdata.model.data.entity.OrderDataDBO;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class IBKROrderToOrderDataTest {

    private IBKROrderToOrderData ibkrOrderToOrderData = new IBKROrderToOrderData();

    private Order createTestOrder(){
        Order order = new Order();
        order.orderId(1);
        order.action(Types.Action.BUY);
        order.totalQuantity(Decimal.ONE);

        order.tif(Types.TimeInForce.DAY);
        order.auxPrice(0);
        order.cashQty(0);
        order.usePriceMgmtAlgo(false);

        return order;
    }

    @Test
    void testConvertLMT(){

        Order order = createTestOrder();
        order.orderType(OrderType.LMT);
        order.lmtPrice(10.95);
        OrderDataDBO data = ibkrOrderToOrderData.convertOrder(order);

        assertEquals(1, data.getId());
        assertEquals(Types.Action.BUY, data.getAction());
        assertEquals(BigDecimal.ONE, data.getTotalQuantity());
        assertEquals(OrderType.LMT, data.getOrderType());
        assertEquals(Types.TimeInForce.DAY, data.getTimeInForce());
        assertEquals(BigDecimal.valueOf(10.95), data.getLimitPrice());
        assertEquals(0, data.getAuctionPrice().compareTo(BigDecimal.ZERO));
        assertEquals(0, data.getCashQuantity().compareTo(BigDecimal.ZERO));
        assertFalse(data.getUsePriceManagementAlgorithm());
    }

    @Test
    void testConvertMKT(){

        Order order = createTestOrder();
        order.orderType(OrderType.MKT);
        OrderDataDBO data = ibkrOrderToOrderData.convertOrder(order);

        assertEquals(1, data.getId());
        assertEquals(Types.Action.BUY, data.getAction());
        assertEquals(BigDecimal.ONE, data.getTotalQuantity());
        assertEquals(OrderType.MKT, data.getOrderType());
        assertEquals(Types.TimeInForce.DAY, data.getTimeInForce());

        //default fallback value
        assertEquals(BigDecimal.valueOf(Double.MAX_VALUE), data.getLimitPrice());
        assertEquals(0, data.getAuctionPrice().compareTo(BigDecimal.ZERO));
        assertEquals(0, data.getCashQuantity().compareTo(BigDecimal.ZERO));
        assertFalse(data.getUsePriceManagementAlgorithm());
    }
}
