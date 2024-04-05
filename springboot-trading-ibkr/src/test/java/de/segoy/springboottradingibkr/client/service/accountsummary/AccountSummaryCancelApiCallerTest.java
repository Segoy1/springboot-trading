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
class AccountSummaryCancelApiCallerTest {

    @Mock
    private EClientSocket client;
    @Mock
    private PropertiesConfig propertiesConfig;
    @InjectMocks
    private AccountSummaryCancelApiCaller accountSummaryCancelApiCaller;

    @Test
    void testCancelCall(){
        when(propertiesConfig.getACCOUNT_SUMMARY_ID()).thenReturn(1);
        accountSummaryCancelApiCaller.callApi();
        verify(client,times(1)).cancelAccountSummary(1);
    }

}
