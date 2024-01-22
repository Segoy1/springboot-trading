package de.segoy.springboottradingibkr.client.service.position.profitandloss;

import com.ib.client.EClientSocket;
import de.segoy.springboottradingibkr.client.service.ApiCallerWithId;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Qualifier("SinglePnLCancelApiCaller")
class SinglePnLCancelApiCaller implements ApiCallerWithId {
    private final EClientSocket client;

    public SinglePnLCancelApiCaller(EClientSocket client) {
        this.client = client;
    }

    @Override
    public void callApi(int id) {
        client.cancelPnLSingle(id);
    }
}
