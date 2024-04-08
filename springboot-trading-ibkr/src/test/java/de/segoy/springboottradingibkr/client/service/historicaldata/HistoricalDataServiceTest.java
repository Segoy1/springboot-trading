package de.segoy.springboottradingibkr.client.service.historicaldata;

import de.segoy.springboottradingdata.model.data.HistoricalDataSettings;
import de.segoy.springboottradingdata.model.data.entity.ContractData;
import de.segoy.springboottradingdata.model.data.entity.HistoricalData;
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
class HistoricalDataServiceTest {

    @Mock
    private UniqueContractDataProvider uniqueContractDataProvider;
    @Mock
    private HistoricalResponseListService historicalResponseListService;
    @InjectMocks
    private HistoricalDataService historicalDataService;

    @Test
    void testWithValidContract(){
        ContractData contractData1 = ContractData.builder().contractId(1).build();
        HistoricalDataSettings settings = HistoricalDataSettings.builder().contractData(contractData1).build();
        ContractData returnContractData = ContractData.builder().id(1L).contractId(1).build();
        HistoricalData data1 = HistoricalData.builder().contractId(1).open(23.0).build();
        HistoricalData data2 = HistoricalData.builder().contractId(2).open(23.3).build();

        when(uniqueContractDataProvider.getExistingContractDataOrCallApi(contractData1))
                .thenReturn(Optional.of(returnContractData));

        when(historicalResponseListService.getResponseList(settings)).thenReturn(List.of(data1,data2));

        List<HistoricalData> list =historicalDataService.requestHistoricalData(settings);

        assertEquals(data1, list.get(0));
        assertEquals(data2, list.get(1));
        assertEquals(1L, settings.getContractData().getId());

        verify(uniqueContractDataProvider, times(1)).getExistingContractDataOrCallApi(contractData1);
        verify(historicalResponseListService,times(1)).getResponseList(settings);
    }
    @Test
    void testWithInvalidContract(){
        ContractData contractData1 = ContractData.builder().contractId(1).build();
        HistoricalDataSettings settings = HistoricalDataSettings.builder().contractData(contractData1).build();


        when(uniqueContractDataProvider.getExistingContractDataOrCallApi(contractData1))
                .thenReturn(Optional.empty());



        List<HistoricalData> list =historicalDataService.requestHistoricalData(settings);

        assertEquals(0, list.size());

        verify(uniqueContractDataProvider, times(1)).getExistingContractDataOrCallApi(contractData1);
        verify(historicalResponseListService,never()).getResponseList(any(HistoricalDataSettings.class));
    }
}
