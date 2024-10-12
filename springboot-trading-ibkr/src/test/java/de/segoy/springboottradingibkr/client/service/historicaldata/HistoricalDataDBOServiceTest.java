package de.segoy.springboottradingibkr.client.service.historicaldata;

import de.segoy.springboottradingdata.model.data.HistoricalDataSettings;
import de.segoy.springboottradingdata.model.data.entity.ContractDataDBO;
import de.segoy.springboottradingdata.model.data.entity.HistoricalDataDBO;
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
class HistoricalDataDBOServiceTest {

    @Mock
    private UniqueContractDataProvider uniqueContractDataProvider;
    @Mock
    private HistoricalResponseListService historicalResponseListService;
    @InjectMocks
    private HistoricalDataService historicalDataService;

    @Test
    void testWithValidContract(){
        ContractDataDBO contractDataDBO1 = ContractDataDBO.builder().contractId(1).build();
        HistoricalDataSettings settings = HistoricalDataSettings.builder().contractDataDBO(contractDataDBO1).build();
        ContractDataDBO returnContractDataDBO = ContractDataDBO.builder().id(1L).contractId(1).build();
        HistoricalDataDBO data1 = HistoricalDataDBO.builder().contractId(1).open(23.0).build();
        HistoricalDataDBO data2 = HistoricalDataDBO.builder().contractId(2).open(23.3).build();

        when(uniqueContractDataProvider.getExistingContractDataOrCallApi(contractDataDBO1))
                .thenReturn(Optional.of(returnContractDataDBO));

        when(historicalResponseListService.getResponseList(settings)).thenReturn(List.of(data1,data2));

        List<HistoricalDataDBO> list =historicalDataService.requestHistoricalData(settings);

        assertEquals(data1, list.get(0));
        assertEquals(data2, list.get(1));
        assertEquals(1L, settings.getContractDataDBO().getId());

        verify(uniqueContractDataProvider, times(1)).getExistingContractDataOrCallApi(contractDataDBO1);
        verify(historicalResponseListService,times(1)).getResponseList(settings);
    }
    @Test
    void testWithInvalidContract(){
        ContractDataDBO contractDataDBO1 = ContractDataDBO.builder().contractId(1).build();
        HistoricalDataSettings settings = HistoricalDataSettings.builder().contractDataDBO(contractDataDBO1).build();


        when(uniqueContractDataProvider.getExistingContractDataOrCallApi(contractDataDBO1))
                .thenReturn(Optional.empty());



        List<HistoricalDataDBO> list =historicalDataService.requestHistoricalData(settings);

        assertEquals(0, list.size());

        verify(uniqueContractDataProvider, times(1)).getExistingContractDataOrCallApi(contractDataDBO1);
        verify(historicalResponseListService,never()).getResponseList(any(HistoricalDataSettings.class));
    }
}
