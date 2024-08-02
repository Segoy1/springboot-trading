package de.segoy.springboottradingibkr.client.service.marketdata;

import de.segoy.springboottradingdata.model.data.entity.ContractData;
import de.segoy.springboottradingdata.repository.ContractDataRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StopAndStartMarketDataServiceTest {

    @Mock
    private ContractDataRepository contractDataRepository;
    @Mock
    private StartMarketDataApiCaller startMarketDataApiCaller;
    @Mock
    private StopMarketDataService stopMarketDataService;
    @InjectMocks
    private StopAndStartMarketDataService stopAndStartMarketDataService;

    @Test
    void testReinitiateApiCall() {
        ContractData contractData = ContractData.builder().build();

        when(contractDataRepository.findById(100L)).thenReturn(Optional.of(contractData));
        stopAndStartMarketDataService.reinitiateApiCall(100);

        verify(stopMarketDataService, times(1)).stopMarketDataForTickerId(100);
        verify(startMarketDataApiCaller, times(1)).callApi(contractData);
    }
    @Test
    void testReinitiateApiCallAbsentData() {

        when(contractDataRepository.findById(100L)).thenReturn(Optional.empty());
        stopAndStartMarketDataService.reinitiateApiCall(100);

        verify(stopMarketDataService, times(1)).stopMarketDataForTickerId(100);
        verify(startMarketDataApiCaller, never()).callApi(any(ContractData.class));
    }
}
