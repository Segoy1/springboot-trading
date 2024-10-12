package de.segoy.springboottradingdata.modelconverter;

import com.ib.client.Contract;
import de.segoy.springboottradingdata.model.data.entity.ContractDataDBO;
import de.segoy.springboottradingdata.model.data.entity.PositionDataDBO;
import de.segoy.springboottradingdata.modelsynchronize.ContractDataDatabaseSynchronizer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.OptionalLong;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IBKRResponseToPositionDataDBOTest {

    @Mock
    private ContractDataDatabaseSynchronizer contractDataDatabaseSynchronizer;
    @InjectMocks
    private IBKRResponseToPositionData ibkrResponseToPositionData;

    @Test
    void testConvertAndPersistContract(){

        Contract contract = new Contract();
        ContractDataDBO contractDataDBO = ContractDataDBO.builder().build();


        when(contractDataDatabaseSynchronizer.findInDBOrConvertAndSaveOrUpdateIfIdIsProvided(OptionalLong.empty(),
                contract)).thenReturn(contractDataDBO);

        PositionDataDBO position = ibkrResponseToPositionData.convertAndPersistContract("Account",contract,
                BigDecimal.TEN, 1.0);

        assertEquals("Account", position.getAccount());
        assertEquals(BigDecimal.TEN, position.getPosition());
        assertEquals(1.0, position.getAverageCost());
        assertEquals(10, position.getTotalCost());

        verify(contractDataDatabaseSynchronizer, times(1)).findInDBOrConvertAndSaveOrUpdateIfIdIsProvided(OptionalLong.empty(), contract);
    }
}
