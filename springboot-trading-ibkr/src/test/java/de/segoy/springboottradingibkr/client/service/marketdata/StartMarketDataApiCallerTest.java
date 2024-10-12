package de.segoy.springboottradingibkr.client.service.marketdata;

import com.ib.client.Contract;
import com.ib.client.EClientSocket;
import com.ib.client.Types;
import de.segoy.springboottradingdata.config.PropertiesConfig;
import de.segoy.springboottradingdata.model.data.entity.ContractDataDBO;
import de.segoy.springboottradingdata.modelconverter.ContractDataToIBKRContract;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StartMarketDataApiCallerTest {

    @Mock
    private EClientSocket client;
    @Mock
    private ContractDataToIBKRContract contractDataToIBKRContract;
    @Mock
    private PropertiesConfig propertiesConfig;
    @InjectMocks
    private StartMarketDataApiCaller startMarketDataApiCaller;

    @Test
    void testCallApi() {
        ContractDataDBO data = ContractDataDBO.builder().id(1L).securityType(Types.SecType.STK).build();
        Contract contract = new Contract();

        when(contractDataToIBKRContract.convertContractData(data)).thenReturn(contract);
        when(propertiesConfig.getGenericTicks()).thenReturn("200, 102");

        startMarketDataApiCaller.callApi(data);

        verify(client, times(1))
                .reqMktData(1, contract, "200, 102",
                        false, false, null);
        verify(contractDataToIBKRContract, times(1)).convertContractData(data);
        verify(propertiesConfig, times(1)).getGenericTicks();
    }
}
