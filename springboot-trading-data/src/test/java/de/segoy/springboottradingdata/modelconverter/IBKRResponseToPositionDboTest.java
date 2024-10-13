package de.segoy.springboottradingdata.modelconverter;

import com.ib.client.Contract;
import de.segoy.springboottradingdata.model.data.entity.ContractDbo;
import de.segoy.springboottradingdata.model.data.entity.PositionDbo;
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
class IBKRResponseToPositionDboTest {

    @Mock
    private ContractDataDatabaseSynchronizer contractDataDatabaseSynchronizer;
    @InjectMocks
    private IBKRToPositionDbo ibkrToPositionDbo;

    @Test
    void testConvertAndPersistContract(){

        Contract contract = new Contract();
        ContractDbo contractDBO = ContractDbo.builder().build();


        when(contractDataDatabaseSynchronizer.findInDBOrConvertAndSaveOrUpdateIfIdIsProvided(OptionalLong.empty(),
                contract)).thenReturn(contractDBO);

        PositionDbo position = ibkrToPositionDbo.convertAndPersistContract("Account",contract,
                BigDecimal.TEN, 1.0);

        assertEquals("Account", position.getAccount());
        assertEquals(BigDecimal.TEN, position.getPosition());
        assertEquals(1.0, position.getAverageCost());
        assertEquals(10, position.getTotalCost());

        verify(contractDataDatabaseSynchronizer, times(1)).findInDBOrConvertAndSaveOrUpdateIfIdIsProvided(OptionalLong.empty(), contract);
    }
}
