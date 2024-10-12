package de.segoy.springboottradingibkr.client.responsehandler;

import de.segoy.springboottradingdata.model.data.entity.ContractDataDBO;
import de.segoy.springboottradingdata.model.data.entity.PositionDataDBO;
import de.segoy.springboottradingdata.modelsynchronize.PositionDataDatabaseSynchronizer;
import de.segoy.springboottradingibkr.client.service.contract.UniqueContractDataProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StreamsAggregatedPositionHandler {

    private final UniqueContractDataProvider uniqueContractDataProvider;
    private final PositionDataDatabaseSynchronizer positionDataDatabaseSynchronizer;

    public PositionDataDBO persistContractAndPositionData(PositionDataDBO positionDataDBO) {
        ContractDataDBO persistedContract =
                uniqueContractDataProvider.getExistingContractDataOrCallApi(positionDataDBO.getContractDataDBO()).orElseThrow();
        positionDataDBO.setContractDataDBO(persistedContract);
        return positionDataDatabaseSynchronizer.updateInDbOrSave(positionDataDBO);
    }
}
