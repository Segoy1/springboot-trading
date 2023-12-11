package de.segoy.springboottradingibkr.client.service.position;

import com.ib.client.EClientSocket;
import de.segoy.springboottradingdata.model.PositionData;
import de.segoy.springboottradingibkr.client.service.ApiCallerWithoutParameter;
import org.springframework.stereotype.Service;

@Service
public class PoistionApiCaller implements ApiCallerWithoutParameter<PositionData> {

    private final EClientSocket client;

    public PoistionApiCaller(EClientSocket client) {
        this.client = client;
    }

    @Override
    public void callApi() {
        client.reqPositions();
    }
}
