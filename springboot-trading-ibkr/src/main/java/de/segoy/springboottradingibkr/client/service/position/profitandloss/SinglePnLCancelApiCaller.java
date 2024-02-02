package de.segoy.springboottradingibkr.client.service.position.profitandloss;

import com.ib.client.EClientSocket;
import de.segoy.springboottradingibkr.client.service.ApiCallerWithId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service("SinglePnLCancelApiCaller")
@RequiredArgsConstructor
class SinglePnLCancelApiCaller implements ApiCallerWithId {

    private final EClientSocket client;

    @Override
    public void callApi(int id) {
        client.cancelPnLSingle(id);
    }
}
