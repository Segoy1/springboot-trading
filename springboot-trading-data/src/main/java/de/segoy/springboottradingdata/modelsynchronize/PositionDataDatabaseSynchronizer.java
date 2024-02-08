package de.segoy.springboottradingdata.modelsynchronize;

import de.segoy.springboottradingdata.model.data.entity.PositionData;
import de.segoy.springboottradingdata.modelconverter.IBKRResponseToPositionData;
import de.segoy.springboottradingdata.repository.PositionDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class PositionDataDatabaseSynchronizer {

    private final IBKRResponseToPositionData ibkrResponseToPositionData;
    private final PositionDataRepository positionDataRepository;


    public PositionData findInDbOrSave(PositionData positionData) {
        return positionDataRepository.findFirstByContractData(positionData.getContractData()).map((dbPositionData) -> {
            dbPositionData.setPosition(positionData.getPosition());
            dbPositionData.setAverageCost(positionData.getAverageCost());
            dbPositionData.setTotalCost(positionData.getTotalCost());

            //delete if Position is 0.
            if(dbPositionData.getPosition().equals(BigDecimal.ZERO)){
                positionDataRepository.delete(dbPositionData);
                return dbPositionData;
            }
            return positionDataRepository.save(dbPositionData);
        }).orElseGet(()-> positionDataRepository.save(positionData));
    }
}
