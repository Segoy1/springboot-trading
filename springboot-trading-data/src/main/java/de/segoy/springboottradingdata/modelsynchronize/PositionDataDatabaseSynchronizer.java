package de.segoy.springboottradingdata.modelsynchronize;

import de.segoy.springboottradingdata.model.data.entity.ContractDbo;
import de.segoy.springboottradingdata.model.data.entity.PositionDbo;
import de.segoy.springboottradingdata.repository.ContractRepository;
import de.segoy.springboottradingdata.repository.PositionRepository;
import jakarta.persistence.EntityManager;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class PositionDataDatabaseSynchronizer {

  private final PositionRepository positionRepository;
  private final EntityManager entityManager;
  private final ContractRepository contractRepository;

  @Transactional
  public Optional<PositionDbo> updateInDbOrSave(PositionDbo positionDBO) {
    return positionRepository
        .findFirstByContractDBO(upsertContract(positionDBO.getContractDBO()))
        .map(
            (dbPositionData) -> {
              dbPositionData.setPosition(positionDBO.getPosition());
              dbPositionData.setAverageCost(positionDBO.getAverageCost());
              dbPositionData.setTotalCost(positionDBO.getTotalCost());

              // delete if Position is 0.
              if (dbPositionData.getPosition().doubleValue() == 0) {
                positionRepository.delete(dbPositionData);
                return Optional.<PositionDbo>empty();
              }
              return saveDboWithPossibleCustomId(dbPositionData);
            })
        .orElseGet(() -> saveDboWithPossibleCustomId(positionDBO));
  }

  private Optional<PositionDbo> saveDboWithPossibleCustomId(PositionDbo positionDBO) {
    if (positionDBO.getPosition().doubleValue() != 0) {
      if (positionDBO.getId() == null) {
        return Optional.of(positionRepository.save(positionDBO));
      } else {
        return Optional.of(entityManager.merge(positionDBO));
      }
    } else {
      return Optional.empty();
    }
  }

  private ContractDbo upsertContract(ContractDbo contractDBO) {
    // OldComment: move UniqueContractDataProvider and use instead
    // Answer not possible or desired, so a simple upsert used instead
    return contractRepository.save(
        contractRepository.findFirstByContractId(contractDBO.getContractId()).orElse(contractDBO));
  }
}
