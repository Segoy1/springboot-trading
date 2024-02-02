package de.segoy.springboottradingdata.modelsynchronize;

import com.ib.client.Contract;
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


    public PositionData findInDbOrSave(String account, Contract contract, BigDecimal position, double avgCost) {
        PositionData positionData = ibkrResponseToPositionData.convertAndPersistContract(account, contract, position,
                avgCost);
        return positionDataRepository.findFirstByContractData(positionData.getContractData()).map((dbPositionData) -> {
            dbPositionData.setPosition(positionData.getPosition());
            dbPositionData.setAverageCost(positionData.getAverageCost());

            //delete if Position is 0.
            if(dbPositionData.getPosition().equals(BigDecimal.ZERO)){
                positionDataRepository.delete(dbPositionData);
                return dbPositionData;
            }
            return positionDataRepository.save(dbPositionData);
        }).orElseGet(()-> positionDataRepository.save(positionData));
    }
}
