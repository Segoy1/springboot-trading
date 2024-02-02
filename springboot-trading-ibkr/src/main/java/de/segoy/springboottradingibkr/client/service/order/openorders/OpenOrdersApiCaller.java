package de.segoy.springboottradingibkr.client.service.order.openorders;

import com.ib.client.EClientSocket;
import de.segoy.springboottradingibkr.client.service.ApiCallerWithoutParameter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service("OpenOrdersApiCaller")
@RequiredArgsConstructor
class OpenOrdersApiCaller implements ApiCallerWithoutParameter {

    private final EClientSocket client;

    @Override
    public void callApi() {
        client.reqOpenOrders();
    }
}
