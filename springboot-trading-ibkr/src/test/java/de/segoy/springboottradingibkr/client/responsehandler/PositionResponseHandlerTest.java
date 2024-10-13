package de.segoy.springboottradingibkr.client.responsehandler;

import com.ib.client.Contract;
import de.segoy.springboottradingdata.model.data.entity.PositionDbo;
import de.segoy.springboottradingdata.modelconverter.IBKRToPositionDbo;
import de.segoy.springboottradingdata.modelsynchronize.PositionDataDatabaseSynchronizer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PositionResponseHandlerTest {

    @Mock
    private IBKRToPositionDbo ibkrToPositionDbo;
    @Mock
    private PositionDataDatabaseSynchronizer positionDataDatabaseSynchronizer;
    @InjectMocks
    private PositionResponseHandler positionResponseHandler;

    @Test
    void transformAndSynch(){
        Contract contract = new Contract();
        PositionDbo data = PositionDbo.builder().build();
        when(ibkrToPositionDbo.convertAndPersistContract("A1", contract, BigDecimal.ONE, 1.1))
                .thenReturn(data);
        when(positionDataDatabaseSynchronizer.updateInDbOrSave(data)).thenReturn(data);

        PositionDbo result =
                positionResponseHandler
                        .transformResponseAndSynchronizeDB("A1",contract,BigDecimal.ONE, 1.1);

        assertThat(result).isEqualTo(data);
    }

}
