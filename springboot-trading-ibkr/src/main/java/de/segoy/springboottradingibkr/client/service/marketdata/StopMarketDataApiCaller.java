package de.segoy.springboottradingibkr.client.service.marketdata;

import com.ib.client.EClientSocket;
import de.segoy.springboottradingibkr.client.service.ApiCallerWithId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service("StopMarketDataApiCaller")
@RequiredArgsConstructor
class StopMarketDataApiCaller implements ApiCallerWithId {

    private final EClientSocket client;

    public void callApi(int id){
        client.cancelMktData(id);
    }
}
