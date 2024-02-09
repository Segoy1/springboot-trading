package de.segoy.springboottradingibkr.client.responsehandler;

import de.segoy.springboottradingdata.model.data.entity.ContractData;
import de.segoy.springboottradingdata.model.data.entity.PositionData;
import de.segoy.springboottradingdata.modelsynchronize.PositionDataDatabaseSynchronizer;
import de.segoy.springboottradingibkr.client.service.contract.UniqueContractDataProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StreamsAggregatedPositionHandler {

    private final UniqueContractDataProvider uniqueContractDataProvider;
    private final PositionDataDatabaseSynchronizer positionDataDatabaseSynchronizer;

    public PositionData persistContractAndPositionData(PositionData positionData) {
        ContractData persistedContract =
                uniqueContractDataProvider.getExistingContractDataOrCallApi(positionData.getContractData()).orElseThrow();
        positionData.setContractData(persistedContract);
        return positionDataDatabaseSynchronizer.findInDbOrSave(positionData);
    }
}
