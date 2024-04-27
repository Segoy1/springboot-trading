package de.segoy.springboottradingibkr.client.service.marketdata;

import com.ib.client.EClientSocket;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class StopMarketDataApiCallerTest {

    @Mock
    private EClientSocket client;
    @InjectMocks
    private StopMarketDataApiCaller stopMarketDataApiCaller;

    @Test
    void testCallApi(){

        stopMarketDataApiCaller.callApi(1);

        verify(client,times(1)).cancelMktData(1);
    }

}
