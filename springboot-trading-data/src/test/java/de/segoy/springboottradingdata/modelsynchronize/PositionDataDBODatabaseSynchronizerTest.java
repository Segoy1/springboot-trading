package de.segoy.springboottradingdata.modelsynchronize;

import de.segoy.springboottradingdata.model.data.entity.ContractDataDBO;
import de.segoy.springboottradingdata.model.data.entity.PositionDataDBO;
import de.segoy.springboottradingdata.repository.PositionDataRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PositionDataDBODatabaseSynchronizerTest {

    @Mock
    private PositionDataRepository positionDataRepository;
    @InjectMocks PositionDataDatabaseSynchronizer positionDataDatabaseSynchronizer;

    @Test
    void testUpdateInDB(){
        ContractDataDBO contract = ContractDataDBO.builder().contractId(1).build();
        PositionDataDBO positionDataDBO =
                PositionDataDBO.builder().contractDataDBO(contract).position(BigDecimal.valueOf(1)).averageCost(1.0).build();

        when(positionDataRepository.findFirstByContractDataDBO(contract)).thenReturn(Optional.of(positionDataDBO));
        when(positionDataRepository.save(positionDataDBO)).thenReturn(positionDataDBO);

        PositionDataDBO result = positionDataDatabaseSynchronizer.updateInDbOrSave(positionDataDBO);

        assertEquals(BigDecimal.ONE, result.getPosition());
        assertEquals(1.0, result.getAverageCost());
        assertEquals(1, result.getContractDataDBO().getContractId());

        verify(positionDataRepository, times(1)).findFirstByContractDataDBO(contract);
        verify(positionDataRepository, times(1)).save(positionDataDBO);
    }

    @Test
    void testSaveToDB(){
        ContractDataDBO contract = ContractDataDBO.builder().contractId(1).build();
        PositionDataDBO positionDataDBO =
                PositionDataDBO.builder().contractDataDBO(contract).position(BigDecimal.valueOf(1)).averageCost(1.0).build();

        when(positionDataRepository.findFirstByContractDataDBO(contract)).thenReturn(Optional.empty());
        when(positionDataRepository.save(positionDataDBO)).thenReturn(positionDataDBO);

        PositionDataDBO result = positionDataDatabaseSynchronizer.updateInDbOrSave(positionDataDBO);

        assertEquals(BigDecimal.ONE, result.getPosition());
        assertEquals(1.0, result.getAverageCost());
        assertEquals(1, result.getContractDataDBO().getContractId());

        verify(positionDataRepository, times(1)).findFirstByContractDataDBO(contract);
        verify(positionDataRepository, times(1)).save(positionDataDBO);
    }
    @Test
    void testDeleteFromDB(){
        ContractDataDBO contract = ContractDataDBO.builder().contractId(1).build();
        PositionDataDBO positionDataDBO =
                PositionDataDBO.builder().contractDataDBO(contract).position(BigDecimal.valueOf(0)).averageCost(1.0).build();

        when(positionDataRepository.findFirstByContractDataDBO(contract)).thenReturn(Optional.of(positionDataDBO));


        PositionDataDBO result = positionDataDatabaseSynchronizer.updateInDbOrSave(positionDataDBO);

        assertEquals(BigDecimal.ZERO, result.getPosition());
        assertEquals(1.0, result.getAverageCost());
        assertEquals(1, result.getContractDataDBO().getContractId());

        verify(positionDataRepository, times(1)).delete(positionDataDBO);
        verify(positionDataRepository, times(1)).findFirstByContractDataDBO(contract);
    }

}
