package de.segoy.springboottradingibkr.client.service.accountsummary.profitandloss;

import com.ib.client.EClientSocket;
import de.segoy.springboottradingdata.config.PropertiesConfig;
import de.segoy.springboottradingdata.model.data.entity.ConnectionDbo;
import de.segoy.springboottradingdata.repository.ConnectionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountPnLApiCallerTest {

    @Mock
    private EClientSocket client;
    @Mock
    private PropertiesConfig propertiesConfig;
    @Mock
    private ConnectionRepository connectionRepository;
    @InjectMocks
    private AccountPnLApiCaller accountPnLApiCaller;

    @Test
    void testApiCall(){
        ConnectionDbo connectionDBO = ConnectionDbo.builder().accountList("A").build();
        when(propertiesConfig.getConnectionId()).thenReturn(1L);
        when(propertiesConfig.getPnlAccountId()).thenReturn(2);
        when(connectionRepository.findById(1L)).thenReturn(Optional.of(connectionDBO));

        accountPnLApiCaller.callApi();

        verify(client, times(1)).reqPnL(2,"A","");
    }
    @Test
    void testApiCallAbsentConnection(){
        when(propertiesConfig.getConnectionId()).thenReturn(1L);
        accountPnLApiCaller.callApi();

        verify(client, never()).reqPnL(Mockito.anyInt(),Mockito.anyString(), Mockito.anyString());
    }

}
