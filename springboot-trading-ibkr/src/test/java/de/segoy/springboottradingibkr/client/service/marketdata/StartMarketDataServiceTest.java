package de.segoy.springboottradingibkr.client.service.marketdata;

import de.segoy.springboottradingdata.model.data.entity.ContractData;
import de.segoy.springboottradingibkr.client.service.contract.UniqueContractDataProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StartMarketDataServiceTest {

    @Mock
    private StartMarketDataApiCaller startMarketDataApiCaller;
    @Mock
    private UniqueContractDataProvider uniqueContractDataProvider;
    @InjectMocks
    private StartMarketDataService startMarketDataService;

    @Test
    void testRequestLiveMarketDataForContractDataPresent(){
        ContractData contractData = ContractData.builder().build();
        ContractData returnedData = ContractData.builder().id(1L).build();
        when(uniqueContractDataProvider.getExistingContractDataOrCallApi(contractData)).thenReturn(Optional.of(returnedData));

        Optional<ContractData> result = startMarketDataService.requestLiveMarketDataForContractData(contractData);

        assertEquals(Optional.of(returnedData), result);

        verify(startMarketDataApiCaller, times(1)).callApi(returnedData);
    }
    @Test
    void testRequestLiveMarketDataForContractDataAbsent(){
        ContractData contractData = ContractData.builder().build();
        when(uniqueContractDataProvider.getExistingContractDataOrCallApi(contractData)).thenReturn(Optional.empty());

        Optional<ContractData> result = startMarketDataService.requestLiveMarketDataForContractData(contractData);

        assertEquals(Optional.empty(), result);

        verify(startMarketDataApiCaller, never()).callApi(any(ContractData.class));
    }

}
