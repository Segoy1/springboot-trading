package de.segoy.springboottradingibkr.client.responsehandler;

import de.segoy.springboottradingdata.model.data.entity.ContractDbo;
import de.segoy.springboottradingdata.model.data.entity.PositionDbo;
import de.segoy.springboottradingdata.model.data.kafka.PositionData;
import de.segoy.springboottradingdata.modelconverter.PositionDataToDbo;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StreamsAggregatedPositionHandlerTest {

    @Mock
    private UniqueContractDataProvider uniqueContractDataProvider;
    @Mock
    private PositionDataDatabaseSynchronizer positionDataDatabaseSynchronizer;
    @Mock
    private PositionDataToDbo positionDataToDbo;
    @InjectMocks
    private StreamsAggregatedPositionHandler streamsAggregatedPositionHandler;

    @Test
    void testError(){
        ContractDbo contractDboOld = ContractDbo.builder().contractId(1).build();
        PositionDbo positionDBO = PositionDbo.builder().contractDBO(contractDboOld).build();
        ContractDbo contractDboNew = ContractDbo.builder().contractId(2).build();

        when(uniqueContractDataProvider.getExistingContractDataOrCallApi(contractDboOld))
                .thenReturn(Optional.empty());
        when(positionDataToDbo.convert(any())).thenReturn(positionDBO);



        Exception e =
                assertThrows(RuntimeException.class, ()->{
                        streamsAggregatedPositionHandler.persistContractAndPositionData(positionDBO.toKafkaPositionData());
                        });

        assertThat(e.getMessage()).isEqualTo("No value present");

    }
    @Test
    void testPersist(){
        ContractDbo contractDboOld = ContractDbo.builder().contractId(1).build();
        PositionDbo positionDBO = PositionDbo.builder().contractDBO(contractDboOld).build();
        ContractDbo contractDboNew = ContractDbo.builder().contractId(2).build();

        when(uniqueContractDataProvider.getExistingContractDataOrCallApi(contractDboOld))
                .thenReturn(Optional.of(contractDboNew));
        when(positionDataDatabaseSynchronizer.updateInDbOrSave(positionDBO)).thenReturn(positionDBO);
        when(positionDataToDbo.convert(any())).thenReturn(positionDBO);
    PositionData result =
        streamsAggregatedPositionHandler.persistContractAndPositionData(
            positionDBO.toKafkaPositionData());

        assertThat(result.getContractData().getContractId()).isEqualTo(2);
    }

}
