package de.segoy.springboottradingibkr.client.responsehandler;

import com.ib.client.Contract;
import de.segoy.springboottradingdata.model.data.entity.PositionDbo;
import de.segoy.springboottradingdata.modelconverter.IBKRToPositionDbo;
import de.segoy.springboottradingdata.modelsynchronize.PositionDataDatabaseSynchronizer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class PositionResponseHandler {

    private final PositionDataDatabaseSynchronizer positionDataDatabaseSynchronizer;
    private final IBKRToPositionDbo ibkrToPositionDbo;

    public PositionDbo transformResponseAndSynchronizeDB(String account, Contract contract, BigDecimal position, double avgCost){
        PositionDbo positionDBO = ibkrToPositionDbo.convertAndPersistContract(account, contract, position,
                avgCost);
        return positionDataDatabaseSynchronizer.updateInDbOrSave(positionDBO);
    }
}
