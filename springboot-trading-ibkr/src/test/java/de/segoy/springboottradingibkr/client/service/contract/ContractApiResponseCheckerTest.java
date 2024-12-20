package de.segoy.springboottradingibkr.client.service.contract;

import de.segoy.springboottradingdata.model.data.entity.ContractDbo;
import de.segoy.springboottradingdata.repository.IBKRDataTypeRepository;
import de.segoy.springboottradingdata.service.RepositoryRefreshService;
import de.segoy.springboottradingibkr.client.errorhandling.ApiResponseErrorHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContractApiResponseCheckerTest {

    @Mock
    private IBKRDataTypeRepository<ContractDbo> repository;
    @Mock
    private RepositoryRefreshService repositoryRefreshService;
    @Mock
    private ApiResponseErrorHandler apiResponseErrorHandler;
    @InjectMocks
    private ContractApiResponseChecker contractApiResponseChecker;

    @Test
    void testNormal(){
        ContractDbo contractDBO = ContractDbo.builder().build();
        when(repository.findById(1L)).thenReturn(Optional.empty(),Optional.of(contractDBO));
        when(apiResponseErrorHandler.isErrorForId(1)).thenReturn(false);

        Optional<ContractDbo> result = contractApiResponseChecker.checkForApiResponseAndUpdate(1);

        assertEquals(contractDBO, result.get());
        verify(apiResponseErrorHandler,times(1)).isErrorForId(1);
        verify(repository, times(3)).findById(1L);
        verify(repositoryRefreshService, times(2)).clearCacheAndWait(repository);
    }
    @Test
    void testInError(){
        ContractDbo contractDBO = ContractDbo.builder().build();
        when(repository.findById(1L)).thenReturn(Optional.empty());
        when(apiResponseErrorHandler.isErrorForId(1)).thenReturn(false,true);

        Optional<ContractDbo> result = contractApiResponseChecker.checkForApiResponseAndUpdate(1);

        assertEquals(Optional.empty(), result);
        verify(apiResponseErrorHandler,times(2)).isErrorForId(1);
        verify(repository, times(3)).findById(1L);
        verify(repositoryRefreshService, times(2)).clearCacheAndWait(repository);

    }
}
