package de.segoy.springboottradingibkr.client.service.historicaldata;

import de.segoy.springboottradingdata.config.PropertiesConfig;
import de.segoy.springboottradingdata.model.data.entity.HistoricalDataDBO;
import de.segoy.springboottradingdata.repository.HistoricalDataRepository;
import de.segoy.springboottradingdata.service.RepositoryRefreshService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HistoricalDataDBOApiResponseCheckerTest {
    @Mock
    private HistoricalDataRepository repository;
    @Mock
    private RepositoryRefreshService repositoryRefreshService;
    @Mock
    private PropertiesConfig propertiesConfig;
    @InjectMocks
    private HistoricalApiResponseChecker historicalDataApiResponseChecker;

    @Test
    void testOnFirstTry() {
        HistoricalDataDBO historicalDataDBO = HistoricalDataDBO.builder().id(2L).contractId(5).build();
        HistoricalDataDBO historicalDataDBO2 = HistoricalDataDBO.builder().id(1L).contractId(5).build();
        List<HistoricalDataDBO> data = new ArrayList<>();
        data.add(historicalDataDBO2);
        data.add(historicalDataDBO);

        Date date = Date.from(Instant.now());
        Set<Integer> callSet = new HashSet<>();
        callSet.add(5);
        callSet.add(3);

        when(propertiesConfig.getFiveSecondsAgo()).thenReturn(date);
        when(repository.findAllByContractId(6)).thenReturn(data);
        when(repository.findAllByContractIdAndCreateDateAfter(6, date)).thenReturn(data);


        List<HistoricalDataDBO> returnList = historicalDataApiResponseChecker.checkForApiResponseAndUpdate(6);

        verify(repositoryRefreshService, times(1)).clearCacheAndWait(repository);
        assertTrue(returnList.contains(historicalDataDBO));
        assertTrue(returnList.contains(historicalDataDBO2));
        assertEquals(2, returnList.size());
    }

}

