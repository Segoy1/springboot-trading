package de.segoy.springboottradingdata.modelsynchronize;

import de.segoy.springboottradingdata.model.data.entity.PositionDbo;
import de.segoy.springboottradingdata.repository.PositionRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PositionDataDatabaseSynchronizer {

  private final PositionRepository positionRepository;
  private final EntityManager entityManager;

  public Optional<PositionDbo> updateInDbOrSave(PositionDbo positionDBO) {
    return positionRepository
        .findFirstByContractDBO(positionDBO.getContractDBO())
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
              return saveDboWithPosibleCustomId(dbPositionData);
            })
        .orElseGet(() -> saveDboWithPosibleCustomId(positionDBO));
  }

  private Optional<PositionDbo> saveDboWithPosibleCustomId(PositionDbo positionDBO) {
    if (positionDBO.getPosition().doubleValue() != 0) {
      if (positionDBO.getId() == null) {
        return Optional.of(positionRepository.save(positionDBO));
      } else {
        return Optional.of(entityManager.merge(positionDBO));
      }
    }else{
        return Optional.empty();
    }
  }
}
