package de.segoy.springboottradingibkr.client.service.position;

import com.ib.client.EClientSocket;
import de.segoy.springboottradingibkr.client.service.ApiCallerWithoutParameter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Qualifier("PoistionApiCaller")
class PoistionApiCaller implements ApiCallerWithoutParameter {

    private final EClientSocket client;

    public PoistionApiCaller(EClientSocket client) {
        this.client = client;
    }

    @Override
    public void callApi() {
        client.reqPositions();
    }
}
