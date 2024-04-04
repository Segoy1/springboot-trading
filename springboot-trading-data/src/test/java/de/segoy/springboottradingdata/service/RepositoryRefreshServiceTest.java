package de.segoy.springboottradingdata.service;

import de.segoy.springboottradingdata.repository.ContractDataRepository;
import jakarta.persistence.Cache;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RepositoryRefreshServiceTest {

    @Mock
    private EntityManager entityManager;
    @Mock
    private EntityManagerFactory entityManagerFactory;
    @Mock
    private ContractDataRepository contractDataRepository;
    @Mock
    private Cache cache;
    @InjectMocks
    private RepositoryRefreshService repositoryRefreshService;

    @Test
    void testClearCacheAndWait(){


        when(entityManager.getEntityManagerFactory()).thenReturn(entityManagerFactory);
        when(entityManagerFactory.getCache()).thenReturn(cache);

        repositoryRefreshService.clearCacheAndWait(contractDataRepository);


        verify(entityManager,times(1)).getEntityManagerFactory();
        verify(entityManagerFactory, times(1)).getCache();
        verify(cache, times(1)).evict(contractDataRepository.getClass());
        verify(entityManager,times(1)).clear();
    }
}
