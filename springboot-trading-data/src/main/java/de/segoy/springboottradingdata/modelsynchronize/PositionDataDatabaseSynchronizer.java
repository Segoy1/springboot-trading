package de.segoy.springboottradingdata.modelsynchronize;

import de.segoy.springboottradingdata.model.data.entity.PositionDataDBO;
import de.segoy.springboottradingdata.repository.PositionDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class PositionDataDatabaseSynchronizer {

    private final PositionDataRepository positionDataRepository;


    public PositionDataDBO updateInDbOrSave(PositionDataDBO positionDataDBO) {
        return positionDataRepository.findFirstByContractDataDBO(positionDataDBO.getContractDataDBO()).map((dbPositionData) -> {
            dbPositionData.setPosition(positionDataDBO.getPosition());
            dbPositionData.setAverageCost(positionDataDBO.getAverageCost());
            dbPositionData.setTotalCost(positionDataDBO.getTotalCost());

            //delete if Position is 0.
            if(dbPositionData.getPosition().equals(BigDecimal.ZERO)){
                positionDataRepository.delete(dbPositionData);
                return dbPositionData;
            }
            return positionDataRepository.save(dbPositionData);
        }).orElseGet(()-> positionDataRepository.save(positionDataDBO));
    }
}
