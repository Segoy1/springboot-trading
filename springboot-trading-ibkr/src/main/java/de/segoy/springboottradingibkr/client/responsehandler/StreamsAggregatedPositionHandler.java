package de.segoy.springboottradingibkr.client.responsehandler;

import de.segoy.springboottradingdata.model.data.entity.ContractDbo;
import de.segoy.springboottradingdata.model.data.entity.PositionDbo;
import de.segoy.springboottradingdata.modelsynchronize.PositionDataDatabaseSynchronizer;
import de.segoy.springboottradingibkr.client.service.contract.UniqueContractDataProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StreamsAggregatedPositionHandler {

    private final UniqueContractDataProvider uniqueContractDataProvider;
    private final PositionDataDatabaseSynchronizer positionDataDatabaseSynchronizer;

    public PositionDbo persistContractAndPositionData(PositionDbo positionDBO) {
        ContractDbo persistedContract =
                uniqueContractDataProvider.getExistingContractDataOrCallApi(positionDBO.getContractDBO()).orElseThrow();
        positionDBO.setContractDBO(persistedContract);
        return positionDataDatabaseSynchronizer.updateInDbOrSave(positionDBO);
    }
}
