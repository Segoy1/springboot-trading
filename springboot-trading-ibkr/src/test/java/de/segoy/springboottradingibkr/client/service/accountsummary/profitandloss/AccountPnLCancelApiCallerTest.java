package de.segoy.springboottradingibkr.client.service.accountsummary.profitandloss;

import com.ib.client.EClientSocket;
import de.segoy.springboottradingdata.config.PropertiesConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountPnLCancelApiCallerTest {

    @Mock
    private EClientSocket client;
    @Mock
    private PropertiesConfig propertiesConfig;
    @InjectMocks
    private AccountPnLCancelApiCaller accountPnLCancelApiCaller;

    @Test
    void testCancelCall(){
        when(propertiesConfig.getPnlAccountId()).thenReturn(1);
        accountPnLCancelApiCaller.callApi();
        verify(client,times(1)).cancelPnL(1);
    }

}
