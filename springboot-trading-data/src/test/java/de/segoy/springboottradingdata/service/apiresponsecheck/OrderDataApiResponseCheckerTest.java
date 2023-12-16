package de.segoy.springboottradingdata.service.apiresponsecheck;

import de.segoy.springboottradingdata.config.PropertiesConfig;
import de.segoy.springboottradingdata.model.OrderData;
import de.segoy.springboottradingdata.repository.OrderDataRepository;
import de.segoy.springboottradingdata.service.RepositoryRefreshService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderDataApiResponseCheckerTest {


    @Mock
    private OrderDataRepository repository;
    @Mock
    private RepositoryRefreshService repositoryRefreshService;
    @InjectMocks
    private OrderDataApiResponseChecker dataApiResponseChecker;

//    @Test
    void testOnFirstTry() {
        OrderData data = OrderData.builder().id(2L).build();


        Set<Integer> callSet = new HashSet<>();
        callSet.add(5);
        callSet.add(3);

        when(repository.findById(2L)).thenReturn(Optional.ofNullable(data));

        Optional<OrderData> orderData = dataApiResponseChecker.checkForApiResponseAndUpdate(2);

        verify(repositoryRefreshService, times(1)).clearCache(repository);
        assertTrue(orderData.isPresent());
        assertEquals(2L, orderData.get().getId());
    }

}