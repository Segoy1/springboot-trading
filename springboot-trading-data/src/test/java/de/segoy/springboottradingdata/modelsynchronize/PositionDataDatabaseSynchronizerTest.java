package de.segoy.springboottradingdata.modelsynchronize;

import de.segoy.springboottradingdata.model.data.entity.ContractData;
import de.segoy.springboottradingdata.model.data.entity.PositionData;
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
class PositionDataDatabaseSynchronizerTest {

    @Mock
    private PositionDataRepository positionDataRepository;
    @InjectMocks PositionDataDatabaseSynchronizer positionDataDatabaseSynchronizer;

    @Test
    void testUpdateInDB(){
        ContractData contract = ContractData.builder().contractId(1).build();
        PositionData positionData =
                PositionData.builder().contractData(contract).position(BigDecimal.valueOf(1)).averageCost(1.0).build();

        when(positionDataRepository.findFirstByContractData(contract)).thenReturn(Optional.of(positionData));
        when(positionDataRepository.save(positionData)).thenReturn(positionData);

        PositionData result = positionDataDatabaseSynchronizer.updateInDbOrSave(positionData);

        assertEquals(BigDecimal.ONE, result.getPosition());
        assertEquals(1.0, result.getAverageCost());
        assertEquals(1, result.getContractData().getContractId());

        verify(positionDataRepository, times(1)).findFirstByContractData(contract);
        verify(positionDataRepository, times(1)).save(positionData);
    }

    @Test
    void testSaveToDB(){
        ContractData contract = ContractData.builder().contractId(1).build();
        PositionData positionData =
                PositionData.builder().contractData(contract).position(BigDecimal.valueOf(1)).averageCost(1.0).build();

        when(positionDataRepository.findFirstByContractData(contract)).thenReturn(Optional.empty());
        when(positionDataRepository.save(positionData)).thenReturn(positionData);

        PositionData result = positionDataDatabaseSynchronizer.updateInDbOrSave(positionData);

        assertEquals(BigDecimal.ONE, result.getPosition());
        assertEquals(1.0, result.getAverageCost());
        assertEquals(1, result.getContractData().getContractId());

        verify(positionDataRepository, times(1)).findFirstByContractData(contract);
        verify(positionDataRepository, times(1)).save(positionData);
    }
    @Test
    void testDeleteFromDB(){
        ContractData contract = ContractData.builder().contractId(1).build();
        PositionData positionData =
                PositionData.builder().contractData(contract).position(BigDecimal.valueOf(0)).averageCost(1.0).build();

        when(positionDataRepository.findFirstByContractData(contract)).thenReturn(Optional.of(positionData));


        PositionData result = positionDataDatabaseSynchronizer.updateInDbOrSave(positionData);

        assertEquals(BigDecimal.ZERO, result.getPosition());
        assertEquals(1.0, result.getAverageCost());
        assertEquals(1, result.getContractData().getContractId());

        verify(positionDataRepository, times(1)).delete(positionData);
        verify(positionDataRepository, times(1)).findFirstByContractData(contract);
    }

}
