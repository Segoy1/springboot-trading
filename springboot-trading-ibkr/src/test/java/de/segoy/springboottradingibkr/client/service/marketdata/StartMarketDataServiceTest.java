package de.segoy.springboottradingibkr.client.service.marketdata;

import de.segoy.springboottradingdata.model.data.entity.ContractDbo;
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
        ContractDbo contractDBO = ContractDbo.builder().build();
        ContractDbo returnedData = ContractDbo.builder().id(1L).build();
        when(uniqueContractDataProvider.getExistingContractDataOrCallApi(contractDBO)).thenReturn(Optional.of(returnedData));

        Optional<ContractDbo> result = startMarketDataService.requestLiveMarketDataForContractData(contractDBO);

        assertEquals(Optional.of(returnedData), result);

        verify(startMarketDataApiCaller, times(1)).callApi(returnedData);
    }
    @Test
    void testRequestLiveMarketDataForContractDataAbsent(){
        ContractDbo contractDBO = ContractDbo.builder().build();
        when(uniqueContractDataProvider.getExistingContractDataOrCallApi(contractDBO)).thenReturn(Optional.empty());

        Optional<ContractDbo> result = startMarketDataService.requestLiveMarketDataForContractData(contractDBO);

        assertEquals(Optional.empty(), result);

        verify(startMarketDataApiCaller, never()).callApi(any(ContractDbo.class));
    }

}
