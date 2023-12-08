package de.segoy.springboottradingdata.modelconverter;

import com.ib.client.Contract;
import com.ib.client.Types;
import de.segoy.springboottradingdata.model.ComboLegData;
import de.segoy.springboottradingdata.model.ContractData;
import de.segoy.springboottradingdata.repository.ComboLegDataRepository;
import de.segoy.springboottradingdata.repository.ContractDataRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DatabaseSyncIBKRContractAndContractDataTest {

    @Mock
    private IBKRContractToContractData ibkrContractToContractData;
    @Mock
    private ContractDataRepository contractDataRepository;
    @Mock
    private ComboLegDataRepository comboLegDataRepository;
    @InjectMocks
    private DatabaseSyncIBKRContractAndContractData databaseSyncIBKRContractAndContractData;

    @Test
    void testWithNotExistentContractIdAndParsedId() {
        ContractData data = buildContract();
        Contract contract = new Contract();
        contract.conid(123);

        when(contractDataRepository.findFirstByContractId(123)).thenReturn(Optional.empty());
        when(ibkrContractToContractData.convertIBKRContract(contract)).thenReturn(data);
        when(comboLegDataRepository.findFirstByContractIdAndActionAndRatioAndExchange(1, Types.Action.BUY,1,"SMART")).thenReturn(Optional.empty());
        when(comboLegDataRepository.findFirstByContractIdAndActionAndRatioAndExchange(2, Types.Action.SELL,2,"SMART")).thenReturn(Optional.empty());
        when(contractDataRepository.save(data)).thenReturn(data);


        ContractData returnData = databaseSyncIBKRContractAndContractData.findInDBOrConvertAndSaveOrUpdateIfIdIsProvided(OptionalLong.of(1), contract);
        verify(contractDataRepository, times(1)).save(data);
        verify(comboLegDataRepository, times(1)).save(data.getComboLegs().get(0));
        verify(comboLegDataRepository, times(1)).save(data.getComboLegs().get(1));
        assertEquals(1L, returnData.getId());
    }

    @Test
    void testWithExistentContractIdAndParsedId() {
        ContractData data = buildContract();
        Contract contract = new Contract();
        contract.conid(123);

        when(contractDataRepository.findFirstByContractId(123)).thenReturn(Optional.of(data));

        ContractData returnData = databaseSyncIBKRContractAndContractData.findInDBOrConvertAndSaveOrUpdateIfIdIsProvided(OptionalLong.of(1), contract);
        verify(contractDataRepository, times(0)).save(data);
        assertNotEquals(1L, returnData.getId());
        assertEquals(123, returnData.getContractId());
        assertEquals("20251111", returnData.getLastTradeDate());
    }

    @Test
    void testaddContractDetailsFromAPIToContractDataInvalidCall() {
        ContractData data = buildContract();
        Contract contract = new Contract();
        contract.conid(123);

        when(contractDataRepository.findFirstByContractId(123)).thenReturn(Optional.empty());
        when(ibkrContractToContractData.convertIBKRContract(contract)).thenReturn(data);
        when(comboLegDataRepository.findFirstByContractIdAndActionAndRatioAndExchange(1, Types.Action.BUY,1,"SMART")).thenReturn(Optional.of(data.getComboLegs().get(0)));
        when(comboLegDataRepository.findFirstByContractIdAndActionAndRatioAndExchange(2, Types.Action.SELL,2,"SMART")).thenReturn(Optional.of(data.getComboLegs().get(1)));
        when(contractDataRepository.save(data)).thenReturn(data);

        ContractData returnData = databaseSyncIBKRContractAndContractData.findInDBOrConvertAndSaveOrUpdateIfIdIsProvided(OptionalLong.empty(), contract);
        verify(contractDataRepository, times(1)).save(data);
        verify(comboLegDataRepository, times(0)).save(data.getComboLegs().get(0));
        verify(comboLegDataRepository, times(0)).save(data.getComboLegs().get(1));
        assertNotEquals(1L,returnData.getId());
    }

    private ContractData buildContract() {
        List<ComboLegData> legs = new ArrayList<>();
        ComboLegData comboLeg = ComboLegData.builder().contractId(1).ratio(1).action(Types.Action.BUY).exchange("SMART").build();

        ComboLegData comboLeg2 = ComboLegData.builder().contractId(2).ratio(2).action(Types.Action.SELL).exchange("SMART").build();
        legs.add(comboLeg);
        legs.add(comboLeg2);

        return ContractData.builder().contractId(123).right(Types.Right.Call).symbol("SPX")
                .securityType(Types.SecType.STK).currency("USD").lastTradeDate("20251111").exchange("SMART").multiplier("100")
                .localSymbol("P BMW  20221216 72 M").tradingClass("SPXW").includeExpired(false).comboLegsDescription("description")
                .strike(BigDecimal.valueOf(72)).comboLegs(legs).build();
    }

}