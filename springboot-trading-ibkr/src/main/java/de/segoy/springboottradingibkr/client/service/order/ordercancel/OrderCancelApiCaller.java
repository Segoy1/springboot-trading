package de.segoy.springboottradingibkr.client.service.order.ordercancel;

import com.ib.client.EClientSocket;
import de.segoy.springboottradingdata.model.data.entity.OrderDbo;
import de.segoy.springboottradingibkr.client.service.ApiCaller;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service("OrderCancelApiCaller")
@RequiredArgsConstructor
class OrderCancelApiCaller implements ApiCaller<OrderDbo> {

    private final EClientSocket client;

    @Override
    public void callApi(OrderDbo orderData) {
        client.cancelOrder(orderData.getId().intValue(), "");
    }
}
