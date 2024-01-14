package de.segoy.springboottradingibkr.client.service.order.openorders;

import com.ib.client.EClientSocket;
import de.segoy.springboottradingibkr.client.service.ApiCallerWithoutParameter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Qualifier("OpenOrdersApiCaller")
class OpenOrdersApiCaller implements ApiCallerWithoutParameter {

    private final EClientSocket client;

    public OpenOrdersApiCaller(EClientSocket client) {
        this.client = client;
    }

    @Override
    public void callApi() {
        client.reqOpenOrders();
    }
}
