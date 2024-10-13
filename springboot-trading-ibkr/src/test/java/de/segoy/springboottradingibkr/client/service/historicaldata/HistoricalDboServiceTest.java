package de.segoy.springboottradingibkr.client.service.historicaldata;

import de.segoy.springboottradingdata.model.data.HistoricalDataSettings;
import de.segoy.springboottradingdata.model.data.entity.ContractDbo;
import de.segoy.springboottradingdata.model.data.entity.HistoricalDbo;
import de.segoy.springboottradingibkr.client.service.contract.UniqueContractDataProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HistoricalDboServiceTest {

    @Mock
    private UniqueContractDataProvider uniqueContractDataProvider;
    @Mock
    private HistoricalResponseListService historicalResponseListService;
    @InjectMocks
    private HistoricalDataService historicalDataService;

    @Test
    void testWithValidContract(){
        ContractDbo contractDbo1 = ContractDbo.builder().contractId(1).build();
        HistoricalDataSettings settings = HistoricalDataSettings.builder().contractDBO(contractDbo1).build();
        ContractDbo returnContractDbo = ContractDbo.builder().id(1L).contractId(1).build();
        HistoricalDbo data1 = HistoricalDbo.builder().contractId(1).open(23.0).build();
        HistoricalDbo data2 = HistoricalDbo.builder().contractId(2).open(23.3).build();

        when(uniqueContractDataProvider.getExistingContractDataOrCallApi(contractDbo1))
                .thenReturn(Optional.of(returnContractDbo));

        when(historicalResponseListService.getResponseList(settings)).thenReturn(List.of(data1,data2));

        List<HistoricalDbo> list =historicalDataService.requestHistoricalData(settings);

        assertEquals(data1, list.get(0));
        assertEquals(data2, list.get(1));
        assertEquals(1L, settings.getContractDBO().getId());

        verify(uniqueContractDataProvider, times(1)).getExistingContractDataOrCallApi(contractDbo1);
        verify(historicalResponseListService,times(1)).getResponseList(settings);
    }
    @Test
    void testWithInvalidContract(){
        ContractDbo contractDbo1 = ContractDbo.builder().contractId(1).build();
        HistoricalDataSettings settings = HistoricalDataSettings.builder().contractDBO(contractDbo1).build();


        when(uniqueContractDataProvider.getExistingContractDataOrCallApi(contractDbo1))
                .thenReturn(Optional.empty());



        List<HistoricalDbo> list =historicalDataService.requestHistoricalData(settings);

        assertEquals(0, list.size());

        verify(uniqueContractDataProvider, times(1)).getExistingContractDataOrCallApi(contractDbo1);
        verify(historicalResponseListService,never()).getResponseList(any(HistoricalDataSettings.class));
    }
}
