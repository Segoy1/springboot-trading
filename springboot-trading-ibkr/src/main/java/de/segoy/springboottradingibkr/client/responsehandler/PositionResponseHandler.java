package de.segoy.springboottradingibkr.client.responsehandler;

import com.ib.client.Contract;
import de.segoy.springboottradingdata.model.data.entity.PositionData;
import de.segoy.springboottradingdata.modelconverter.IBKRResponseToPositionData;
import de.segoy.springboottradingdata.modelsynchronize.PositionDataDatabaseSynchronizer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class PositionResponseHandler {

    private final PositionDataDatabaseSynchronizer positionDataDatabaseSynchronizer;
    private final IBKRResponseToPositionData ibkrResponseToPositionData;

    public PositionData transformResponseAndSynchronizeDB(String account, Contract contract, BigDecimal position, double avgCost){
        PositionData positionData = ibkrResponseToPositionData.convertAndPersistContract(account, contract, position,
                avgCost);
        return positionDataDatabaseSynchronizer.updateInDbOrSave(positionData);
    }
}
