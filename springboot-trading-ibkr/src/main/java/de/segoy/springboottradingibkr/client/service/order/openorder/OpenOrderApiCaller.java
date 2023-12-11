package de.segoy.springboottradingibkr.client.service.order.openorder;

import com.ib.client.EClientSocket;
import de.segoy.springboottradingdata.model.OrderData;
import de.segoy.springboottradingibkr.client.service.ApiCallerWithoutParameter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Qualifier("OpenOrderApiCaller")
class OpenOrderApiCaller implements ApiCallerWithoutParameter<OrderData> {

    private final EClientSocket client;

    public OpenOrderApiCaller(EClientSocket client) {
        this.client = client;
    }

    @Override
    public void callApi() {
        client.reqAllOpenOrders();
    }
}
