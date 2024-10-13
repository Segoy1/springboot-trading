package de.segoy.springboottradingdata.modelsynchronize;

import com.ib.client.Contract;
import com.ib.client.Types;
import de.segoy.springboottradingdata.model.data.entity.ComboLegDbo;
import de.segoy.springboottradingdata.model.data.entity.ContractDbo;
import de.segoy.springboottradingdata.model.subtype.Symbol;
import de.segoy.springboottradingdata.modelconverter.IBKRToContractDbo;
import de.segoy.springboottradingdata.repository.ComboLegRepository;
import de.segoy.springboottradingdata.repository.ContractRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalLong;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DatabaseSyncIBKRContractAndContractDboTest {

    @Mock
    private IBKRToContractDbo ibkrToContractDbo;
    @Mock
    private ContractRepository contractRepository;
    @Mock
    private ComboLegRepository comboLegRepository;
    @InjectMocks
    private ContractDataDatabaseSynchronizer contractDataDatabaseSynchronizer;

    @Test
    void testWithNotExistentContractIdAndParsedId() {
        ContractDbo data = buildContract();
        Contract contract = new Contract();
        contract.conid(123);

        when(contractRepository.findFirstByContractId(123)).thenReturn(Optional.empty());
        when(ibkrToContractDbo.convertIBKRContract(contract)).thenReturn(data);
        when(contractRepository.save(data)).thenReturn(data);


        ContractDbo returnData = contractDataDatabaseSynchronizer.findInDBOrConvertAndSaveOrUpdateIfIdIsProvided(OptionalLong.of(1), contract);
        verify(contractRepository, times(1)).save(data);
        assertEquals(1L, returnData.getId());
    }

    @Test
    void testWithExistentContractIdAndParsedId() {
        ContractDbo data = buildContract();
        Contract contract = new Contract();
        contract.conid(123);

        when(contractRepository.findFirstByContractId(123)).thenReturn(Optional.of(data));

        ContractDbo returnData = contractDataDatabaseSynchronizer.findInDBOrConvertAndSaveOrUpdateIfIdIsProvided(OptionalLong.of(1), contract);
        verify(contractRepository, times(0)).save(data);
        assertNotEquals(1L, returnData.getId());
        assertEquals(123, returnData.getContractId());
        assertEquals("20251111", returnData.getLastTradeDate());
    }

    @Test
    void testaddContractDetailsFromAPIToContractDataInvalidCall() {
        ContractDbo data = buildContract();
        Contract contract = new Contract();
        contract.conid(123);

        when(contractRepository.findFirstByContractId(123)).thenReturn(Optional.empty());
        when(ibkrToContractDbo.convertIBKRContract(contract)).thenReturn(data);
       when(contractRepository.save(data)).thenReturn(data);

        ContractDbo returnData = contractDataDatabaseSynchronizer.findInDBOrConvertAndSaveOrUpdateIfIdIsProvided(OptionalLong.empty(), contract);
        verify(contractRepository, times(1)).save(data);
        verify(comboLegRepository, times(0)).save(data.getComboLegs().get(0));
        verify(comboLegRepository, times(0)).save(data.getComboLegs().get(1));
        assertNotEquals(1L,returnData.getId());
    }

    private ContractDbo buildContract() {
        List<ComboLegDbo> legs = new ArrayList<>();
        ComboLegDbo comboLeg = ComboLegDbo.builder().contractId(1).ratio(1).action(Types.Action.BUY).exchange("SMART").build();

        ComboLegDbo comboLeg2 = ComboLegDbo.builder().contractId(2).ratio(2).action(Types.Action.SELL).exchange("SMART").build();
        legs.add(comboLeg);
        legs.add(comboLeg2);

        return ContractDbo.builder().contractId(123).right(Types.Right.Call).symbol(Symbol.SPX)
                .securityType(Types.SecType.STK).currency("USD").lastTradeDate("20251111").exchange("SMART").multiplier("100")
                .localSymbol("P BMW  20221216 72 M").tradingClass("SPXW").includeExpired(false).comboLegsDescription("description")
                .strike(BigDecimal.valueOf(72)).comboLegs(legs).build();
    }

}
