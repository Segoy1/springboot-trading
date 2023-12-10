package de.segoy.springboottradingibkr.client.service.marketdata;

import com.ib.client.EClientSocket;
import org.springframework.stereotype.Service;

@Service
class StopMarketDataApiCaller {

    private final EClientSocket client;

    public StopMarketDataApiCaller(EClientSocket client) {
        this.client = client;
    }

    public void callApi(int id){
        client.cancelMktData(id);
    }
}
