package de.segoy.springboottradingibkr.client.service.marketdata;

import de.segoy.springboottradingdata.model.data.entity.ContractDataDBO;
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
        ContractDataDBO contractDataDBO = ContractDataDBO.builder().build();
        ContractDataDBO returnedData = ContractDataDBO.builder().id(1L).build();
        when(uniqueContractDataProvider.getExistingContractDataOrCallApi(contractDataDBO)).thenReturn(Optional.of(returnedData));

        Optional<ContractDataDBO> result = startMarketDataService.requestLiveMarketDataForContractData(contractDataDBO);

        assertEquals(Optional.of(returnedData), result);

        verify(startMarketDataApiCaller, times(1)).callApi(returnedData);
    }
    @Test
    void testRequestLiveMarketDataForContractDataAbsent(){
        ContractDataDBO contractDataDBO = ContractDataDBO.builder().build();
        when(uniqueContractDataProvider.getExistingContractDataOrCallApi(contractDataDBO)).thenReturn(Optional.empty());

        Optional<ContractDataDBO> result = startMarketDataService.requestLiveMarketDataForContractData(contractDataDBO);

        assertEquals(Optional.empty(), result);

        verify(startMarketDataApiCaller, never()).callApi(any(ContractDataDBO.class));
    }

}
