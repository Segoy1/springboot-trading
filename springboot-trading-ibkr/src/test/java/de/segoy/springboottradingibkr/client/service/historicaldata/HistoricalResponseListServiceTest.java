package de.segoy.springboottradingibkr.client.service.historicaldata;

import de.segoy.springboottradingdata.model.data.HistoricalDataSettings;
import de.segoy.springboottradingdata.model.data.entity.ContractDataDBO;
import de.segoy.springboottradingdata.model.data.entity.HistoricalDataDBO;
import de.segoy.springboottradingibkr.client.service.ApiCaller;
import de.segoy.springboottradingibkr.client.service.ListApiResponseChecker;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HistoricalResponseListServiceTest {

    @Mock
    private ListApiResponseChecker<HistoricalDataDBO> historicalDataApiResponseChecker;
    @Mock
    private ApiCaller<HistoricalDataSettings> historicalDataApiCaller;
    @InjectMocks
    private HistoricalResponseListService historicalResponseListService;

    @Test
    void testService(){
        ContractDataDBO contractDataDBO = ContractDataDBO.builder().contractId(1).build();
        HistoricalDataSettings settings = HistoricalDataSettings.builder().contractDataDBO(contractDataDBO).build();
        HistoricalDataDBO data = HistoricalDataDBO.builder().contractId(1).build();
        HistoricalDataDBO data2 =
                HistoricalDataDBO.builder().contractId(1).build();
        when(historicalDataApiResponseChecker.checkForApiResponseAndUpdate(1)).thenReturn(List.of(data,data2));

        List<HistoricalDataDBO> list = historicalResponseListService.getResponseList(settings);

        assertEquals(data2, list.get(1));
        assertEquals(data, list.get(0));

        verify(historicalDataApiCaller, times(1)).callApi(settings);
    }
}
