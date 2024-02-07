package de.segoy.springboottradingibkr.client.service.position;

import com.ib.client.EClientSocket;
import de.segoy.springboottradingibkr.client.service.ApiCallerWithoutParameter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service("PoistionApiCaller")
@RequiredArgsConstructor
class PositionApiCaller implements ApiCallerWithoutParameter {

    private final EClientSocket client;

    @Override
    public void callApi() {
        client.reqPositions();
    }
}
