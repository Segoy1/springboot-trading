package de.segoy.springboottradingdata.modelsynchronize;

import de.segoy.springboottradingdata.model.data.entity.ContractDbo;
import de.segoy.springboottradingdata.model.data.entity.PositionDbo;
import de.segoy.springboottradingdata.repository.ContractRepository;
import de.segoy.springboottradingdata.repository.PositionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PositionDboDatabaseSynchronizerTest {

  @Mock private PositionRepository positionRepository;
  @Mock private ContractRepository contractRepository;
  @InjectMocks PositionDataDatabaseSynchronizer positionDataDatabaseSynchronizer;

  @BeforeEach
  void setup() {
    when(contractRepository.findFirstByContractId(anyInt())).thenReturn(Optional.empty());
    when(contractRepository.save(any(ContractDbo.class)))
        .thenAnswer(
            (Answer<ContractDbo>) invocation -> invocation.getArgument(0));
  }

  @Test
  void testUpdateInDB() {
    ContractDbo contract = ContractDbo.builder().contractId(1).build();
    PositionDbo positionDBO =
        PositionDbo.builder()
            .contractDBO(contract)
            .position(BigDecimal.ONE)
            .averageCost(BigDecimal.ONE)
            .build();

    when(positionRepository.findFirstByContractDBO(contract)).thenReturn(Optional.of(positionDBO));
    when(positionRepository.save(positionDBO)).thenReturn(positionDBO);

    PositionDbo result = positionDataDatabaseSynchronizer.updateInDbOrSave(positionDBO).get();

    assertEquals(BigDecimal.ONE, result.getPosition());
    assertEquals(BigDecimal.ONE, result.getAverageCost());
    assertEquals(1, result.getContractDBO().getContractId());

    verify(positionRepository, times(1)).findFirstByContractDBO(contract);
    verify(positionRepository, times(1)).save(positionDBO);
  }

  @Test
  void testSaveToDB() {
    ContractDbo contract = ContractDbo.builder().contractId(1).build();
    PositionDbo positionDBO =
        PositionDbo.builder()
            .contractDBO(contract)
            .position(BigDecimal.valueOf(1))
            .averageCost(BigDecimal.ONE)
            .build();

    when(positionRepository.findFirstByContractDBO(contract)).thenReturn(Optional.empty());
    when(positionRepository.save(positionDBO)).thenReturn(positionDBO);

    PositionDbo result = positionDataDatabaseSynchronizer.updateInDbOrSave(positionDBO).get();

    assertEquals(BigDecimal.ONE, result.getPosition());
    assertEquals(BigDecimal.ONE, result.getAverageCost());
    assertEquals(1, result.getContractDBO().getContractId());

    verify(positionRepository, times(1)).findFirstByContractDBO(contract);
    verify(positionRepository, times(1)).save(positionDBO);
  }

  @Test
  void testDeleteFromDB() {
    ContractDbo contract = ContractDbo.builder().contractId(1).build();
    PositionDbo positionDBO =
        PositionDbo.builder()
            .contractDBO(contract)
            .position(BigDecimal.valueOf(0))
            .averageCost(BigDecimal.ONE)
            .build();

    when(positionRepository.findFirstByContractDBO(contract)).thenReturn(Optional.of(positionDBO));

    Optional<PositionDbo> result = positionDataDatabaseSynchronizer.updateInDbOrSave(positionDBO);

    assertEquals(result, Optional.empty());

    verify(positionRepository, times(1)).delete(positionDBO);
    verify(positionRepository, times(1)).findFirstByContractDBO(contract);
  }
}
