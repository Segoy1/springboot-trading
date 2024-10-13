package de.segoy.springboottradingibkr.client.service.marketdata;

import de.segoy.springboottradingdata.model.data.entity.ContractDbo;
import de.segoy.springboottradingdata.repository.ContractRepository;
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
    private ContractRepository contractRepository;
    @Mock
    private StartMarketDataApiCaller startMarketDataApiCaller;
    @Mock
    private StopMarketDataService stopMarketDataService;
    @InjectMocks
    private StopAndStartMarketDataService stopAndStartMarketDataService;

    @Test
    void testReinitiateApiCall() {
        ContractDbo contractDBO = ContractDbo.builder().build();

        when(contractRepository.findById(100L)).thenReturn(Optional.of(contractDBO));
        stopAndStartMarketDataService.reinitiateApiCall(100);

        verify(stopMarketDataService, times(1)).stopMarketDataForTickerId(100);
        verify(startMarketDataApiCaller, times(1)).callApi(contractDBO);
    }
    @Test
    void testReinitiateApiCallAbsentData() {

        when(contractRepository.findById(100L)).thenReturn(Optional.empty());
        stopAndStartMarketDataService.reinitiateApiCall(100);

        verify(stopMarketDataService, times(1)).stopMarketDataForTickerId(100);
        verify(startMarketDataApiCaller, never()).callApi(any(ContractDbo.class));
    }
}
