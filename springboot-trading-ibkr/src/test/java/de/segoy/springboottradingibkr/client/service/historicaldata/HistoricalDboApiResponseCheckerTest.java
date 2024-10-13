package de.segoy.springboottradingibkr.client.service.historicaldata;

import de.segoy.springboottradingdata.config.PropertiesConfig;
import de.segoy.springboottradingdata.model.data.entity.HistoricalDbo;
import de.segoy.springboottradingdata.repository.HistoricalRepository;
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
class HistoricalDboApiResponseCheckerTest {
    @Mock
    private HistoricalRepository repository;
    @Mock
    private RepositoryRefreshService repositoryRefreshService;
    @Mock
    private PropertiesConfig propertiesConfig;
    @InjectMocks
    private HistoricalApiResponseChecker historicalDataApiResponseChecker;

    @Test
    void testOnFirstTry() {
        HistoricalDbo historicalDbo = HistoricalDbo.builder().id(2L).contractId(5).build();
        HistoricalDbo historicalDbo2 = HistoricalDbo.builder().id(1L).contractId(5).build();
        List<HistoricalDbo> data = new ArrayList<>();
        data.add(historicalDbo2);
        data.add(historicalDbo);

        Date date = Date.from(Instant.now());
        Set<Integer> callSet = new HashSet<>();
        callSet.add(5);
        callSet.add(3);

        when(propertiesConfig.getFiveSecondsAgo()).thenReturn(date);
        when(repository.findAllByContractId(6)).thenReturn(data);
        when(repository.findAllByContractIdAndCreateDateAfter(6, date)).thenReturn(data);


        List<HistoricalDbo> returnList = historicalDataApiResponseChecker.checkForApiResponseAndUpdate(6);

        verify(repositoryRefreshService, times(1)).clearCacheAndWait(repository);
        assertTrue(returnList.contains(historicalDbo));
        assertTrue(returnList.contains(historicalDbo2));
        assertEquals(2, returnList.size());
    }

}

