package de.segoy.springboottradingibkr.client.responsehandler;

import de.segoy.springboottradingdata.model.data.entity.ContractDataDBO;
import de.segoy.springboottradingdata.model.data.entity.PositionDataDBO;
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
        ContractDataDBO contractDataDBOOld = ContractDataDBO.builder().contractId(1).build();
        PositionDataDBO positionDataDBO = PositionDataDBO.builder().contractDataDBO(contractDataDBOOld).build();
        ContractDataDBO contractDataDBONew = ContractDataDBO.builder().contractId(2).build();

        when(uniqueContractDataProvider.getExistingContractDataOrCallApi(contractDataDBOOld))
                .thenReturn(Optional.empty());



        Exception e =
                assertThrows(RuntimeException.class, ()->{
                        streamsAggregatedPositionHandler.persistContractAndPositionData(positionDataDBO);
                        });

        assertThat(e.getMessage()).isEqualTo("No value present");

    }
    @Test
    void testPersist(){
        ContractDataDBO contractDataDBOOld = ContractDataDBO.builder().contractId(1).build();
        PositionDataDBO positionDataDBO = PositionDataDBO.builder().contractDataDBO(contractDataDBOOld).build();
        ContractDataDBO contractDataDBONew = ContractDataDBO.builder().contractId(2).build();

        when(uniqueContractDataProvider.getExistingContractDataOrCallApi(contractDataDBOOld))
                .thenReturn(Optional.of(contractDataDBONew));
        when(positionDataDatabaseSynchronizer.updateInDbOrSave(positionDataDBO)).thenReturn(positionDataDBO);
        PositionDataDBO result = streamsAggregatedPositionHandler.persistContractAndPositionData(positionDataDBO);

        assertThat(result.getContractDataDBO().getContractId()).isEqualTo(2);
    }

}
