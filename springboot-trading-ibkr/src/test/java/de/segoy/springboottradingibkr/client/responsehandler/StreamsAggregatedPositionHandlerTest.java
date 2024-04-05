package de.segoy.springboottradingibkr.client.responsehandler;

import de.segoy.springboottradingdata.model.data.entity.ContractData;
import de.segoy.springboottradingdata.model.data.entity.PositionData;
import de.segoy.springboottradingdata.modelsynchronize.PositionDataDatabaseSynchronizer;
import de.segoy.springboottradingibkr.client.service.contract.UniqueContractDataProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StreamsAggregatedPositionHandlerTest {

    @Mock
    private UniqueContractDataProvider uniqueContractDataProvider;
    @Mock
    private PositionDataDatabaseSynchronizer positionDataDatabaseSynchronizer;
    @InjectMocks
    private StreamsAggregatedPositionHandler streamsAggregatedPositionHandler;

    @Test
    void testError(){
        ContractData contractDataOld = ContractData.builder().contractId(1).build();
        PositionData positionData = PositionData.builder().contractData(contractDataOld).build();
        ContractData contractDataNew = ContractData.builder().contractId(2).build();

        when(uniqueContractDataProvider.getExistingContractDataOrCallApi(contractDataOld))
                .thenReturn(Optional.empty());



        Exception e =
                assertThrows(RuntimeException.class, ()->{
                        streamsAggregatedPositionHandler.persistContractAndPositionData(positionData);
                        });

        assertThat(e.getMessage()).isEqualTo("No value present");

    }
    @Test
    void testPersist(){
        ContractData contractDataOld = ContractData.builder().contractId(1).build();
        PositionData positionData = PositionData.builder().contractData(contractDataOld).build();
        ContractData contractDataNew = ContractData.builder().contractId(2).build();

        when(uniqueContractDataProvider.getExistingContractDataOrCallApi(contractDataOld))
                .thenReturn(Optional.of(contractDataNew));
        when(positionDataDatabaseSynchronizer.updateInDbOrSave(positionData)).thenReturn(positionData);
        PositionData result = streamsAggregatedPositionHandler.persistContractAndPositionData(positionData);

        assertThat(result.getContractData().getContractId()).isEqualTo(2);
    }

}
