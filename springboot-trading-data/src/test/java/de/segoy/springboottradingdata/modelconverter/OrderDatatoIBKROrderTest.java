package de.segoy.springboottradingdata.modelconverter;

import com.ib.client.Decimal;
import com.ib.client.Order;
import com.ib.client.OrderType;
import com.ib.client.Types;
import de.segoy.springboottradingdata.model.OrderData;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderDatatoIBKROrderTest {

    private OrderDataToIBKROrder orderDatatoIBKROrder;

    @Test
    void testConvertLimitOrder() {
        orderDatatoIBKROrder = new OrderDataToIBKROrder();

        Order order = orderDatatoIBKROrder.convertOrderData(
                OrderData.builder()
                        .orderId(1)
                        .totalQuantity(BigDecimal.valueOf(1))
                        .limitPrice(BigDecimal.valueOf(12))
                        .orderType(OrderType.LMT)
                        .action(Types.Action.BUY).build());

    assertEquals(order.orderId(), 1);
    assertEquals(Decimal.ONE, order.totalQuantity());
    assertEquals(12, order.lmtPrice());
    assertEquals(OrderType.LMT, order.orderType());
    assertEquals(Types.Action.BUY, order.action());
    }

}