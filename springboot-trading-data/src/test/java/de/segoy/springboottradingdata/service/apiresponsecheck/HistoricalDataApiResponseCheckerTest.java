package de.segoy.springboottradingdata.service.apiresponsecheck;

import de.segoy.springboottradingdata.config.PropertiesConfig;
import de.segoy.springboottradingdata.model.HistoricalData;
import de.segoy.springboottradingdata.repository.HistoricalDataRepository;
import de.segoy.springboottradingdata.service.RepositoryRefreshService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HistoricalDataApiResponseCheckerTest {
    @Mock
    private HistoricalDataRepository repository;
    @Mock
    private RepositoryRefreshService repositoryRefreshService;
    @Mock
    private PropertiesConfig propertiesConfig;
    @InjectMocks
    private HistoricalDataApiResponseChecker historicalDataApiResponseChecker;

    @Test
    void testOnFirstTry(){
        HistoricalData historicalData = HistoricalData.builder().id(2L).contractId(5).build();
        HistoricalData historicalData2 = HistoricalData.builder().id(1L).contractId(5).build();
        List<HistoricalData> data= new ArrayList<>();
        data.add(historicalData2);
        data.add(historicalData);

        Set<Integer>  callSet = new HashSet<>();
        callSet.add(5);
        callSet.add(3);

        when(repository.findAllByContractId(6)).thenReturn(data);
        when(propertiesConfig.getActiveApiCalls()).thenReturn(callSet);


        List<HistoricalData> returnList = historicalDataApiResponseChecker.checkForApiResponseAndUpdate(6);

        verify(repositoryRefreshService, times(1)).clearCacheAndWait(repository);
        assertTrue(returnList.contains(historicalData));
        assertTrue(returnList.contains(historicalData2));
        assertEquals(2, returnList.size());
    }

}