package de.segoy.springboottradingibkr.client.service.accountsummary;

import com.ib.client.EClientSocket;
import de.segoy.springboottradingdata.config.PropertiesConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountSummaryApiCallerTest {

    @Mock
    private EClientSocket client;
    @Mock
    private PropertiesConfig propertiesConfig;
    @InjectMocks
    private AccountSummaryApiCaller accountSummaryApiCaller;

    @Test
    void testApiCall(){
        when(propertiesConfig.getACCRUED_CASH()).thenReturn("A");
        when(propertiesConfig.getBUYING_POWER()).thenReturn("B");
        when(propertiesConfig.getNET_LIQUIDATION()).thenReturn("N");
        when(propertiesConfig.getACCOUNT_SUMMARY_ID()).thenReturn(1);

        accountSummaryApiCaller.callApi();

        verify(client, times(1)).reqAccountSummary(1, "All", "A,B,N");
    }
}
