package de.segoy.springboottradingibkr.client.service.order.ordercancel;

import com.ib.client.EClientSocket;
import de.segoy.springboottradingdata.model.OrderData;
import de.segoy.springboottradingibkr.client.service.ApiCaller;
import org.springframework.stereotype.Service;

@Service
class OrderCancelApiCaller implements ApiCaller<OrderData> {

    private final EClientSocket client;

    public OrderCancelApiCaller(EClientSocket client) {
        this.client = client;
    }

    @Override
    public void callApi(OrderData orderData) {
        client.cancelOrder(orderData.getId().intValue(),"");
    }
}
