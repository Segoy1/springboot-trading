package de.segoy.springboottradingibkr.client.responsehandler;

import de.segoy.springboottradingdata.model.data.entity.ContractDbo;
import de.segoy.springboottradingdata.model.data.entity.PositionDbo;
import de.segoy.springboottradingdata.model.data.kafka.PositionData;
import de.segoy.springboottradingdata.modelconverter.PositionDataToDbo;
import de.segoy.springboottradingdata.modelsynchronize.PositionDataDatabaseSynchronizer;
import de.segoy.springboottradingibkr.client.service.contract.UniqueContractDataProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StreamsAggregatedPositionHandler {

    private final UniqueContractDataProvider uniqueContractDataProvider;
    private final PositionDataDatabaseSynchronizer positionDataDatabaseSynchronizer;
    private final PositionDataToDbo positionDataToDbo;

    public PositionData persistContractAndPositionData(PositionData positionData) {
        PositionDbo positionDbo = positionDataToDbo.convert(positionData);
        ContractDbo persistedContract =
                uniqueContractDataProvider.getExistingContractDataOrCallApi(positionDbo.getContractDBO()).orElseThrow();
        positionDbo.setContractDBO(persistedContract);
        return positionDataDatabaseSynchronizer.updateInDbOrSave(positionDbo).toKafkaPositionData();
    }
}
