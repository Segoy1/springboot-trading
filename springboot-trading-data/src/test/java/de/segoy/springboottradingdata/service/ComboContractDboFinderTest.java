package de.segoy.springboottradingdata.service;

import com.ib.client.Types;
import de.segoy.springboottradingdata.model.data.entity.ComboLegDbo;
import de.segoy.springboottradingdata.model.data.entity.ContractDbo;
import de.segoy.springboottradingdata.model.subtype.Symbol;
import de.segoy.springboottradingdata.repository.ContractRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ComboContractDboFinderTest {
    @Mock
    private ContractRepository contractRepository;
    @InjectMocks
    private ComboContractDataFinder comboContractDataFinder;



    List<ComboLegDbo> buildLegList() {

        final List<ComboLegDbo> legs = new ArrayList<>();
        legs.add(ComboLegDbo.builder().contractId(1).id(0L).ratio(1).action(Types.Action.BUY).build());
        legs.add(ComboLegDbo.builder().contractId(2).id(1L).ratio(2).action(Types.Action.SELL).build());
        legs.add(ComboLegDbo.builder().contractId(3).id(2L).ratio(1).action(Types.Action.SELL).build());
        legs.add(ComboLegDbo.builder().contractId(4).id(3L).ratio(3).action(Types.Action.BUY).build());
        return legs;
    }

    @Test
    void testNoExistingContractsForAllLegs() {
        when(contractRepository.findByComboLegsDescriptionContains("1")).thenReturn(new ArrayList<>());

        OptionalLong result = comboContractDataFinder.checkContractWithComboLegs(buildLegList());

        assertEquals(OptionalLong.empty(), result);

    }

    @Test
    void testExistingContractForFirstThreeLegs() {
        ContractDbo contractDBO = ContractDbo.builder().contractId(123).symbol(Symbol.SPX).id(90000000L).build();
        when(contractRepository.findByComboLegsDescriptionContains("1")).thenReturn(new ArrayList<>(List.of(
                contractDBO)));
        when(contractRepository.findByComboLegsDescriptionContains("2")).thenReturn(new ArrayList<>(List.of(
                contractDBO)));
        when(contractRepository.findByComboLegsDescriptionContains("3")).thenReturn(new ArrayList<>(List.of(
                contractDBO)));
        when(contractRepository.findByComboLegsDescriptionContains("4")).thenReturn(new ArrayList<>());

        OptionalLong result = comboContractDataFinder.checkContractWithComboLegs(buildLegList());

        verify(contractRepository,times(1)).findByComboLegsDescriptionContains("1");
        verify(contractRepository,times(1)).findByComboLegsDescriptionContains("2");
        verify(contractRepository,times(1)).findByComboLegsDescriptionContains("3");
        verify(contractRepository,times(1)).findByComboLegsDescriptionContains("4");
        assertEquals(OptionalLong.empty(), result);
    }

    @Test
    void testExisitingContractForAllLegs() {
        ContractDbo contractDBO = ContractDbo.builder().contractId(123).symbol(Symbol.SPX).id(90000000L).build();
        ContractDbo contractDbo2 = ContractDbo.builder().contractId(1234).symbol(Symbol.SPX).id(90000001L).build();
        ContractDbo contractDbo3 = ContractDbo.builder().contractId(12345).symbol(Symbol.SPX).id(90000002L).build();
        ContractDbo contractDbo4 = ContractDbo.builder().contractId(123456).symbol(Symbol.SPX).id(90000003L).build();
        when(contractRepository.findByComboLegsDescriptionContains("1")).thenReturn(new ArrayList<>(List.of(
                contractDBO, contractDbo3, contractDbo4)));
        when(contractRepository.findByComboLegsDescriptionContains("2")).thenReturn(new ArrayList<>(List.of(
                contractDBO, contractDbo3)));
        when(contractRepository.findByComboLegsDescriptionContains("3")).thenReturn(new ArrayList<>(List.of(
                contractDBO, contractDbo2, contractDbo4)));
        when(contractRepository.findByComboLegsDescriptionContains("4")).thenReturn(new ArrayList<>(List.of(
                contractDbo4, contractDbo3, contractDbo2, contractDBO)));

        OptionalLong result = comboContractDataFinder.checkContractWithComboLegs(buildLegList());

        verify(contractRepository,times(1)).findByComboLegsDescriptionContains("1");
        verify(contractRepository,times(1)).findByComboLegsDescriptionContains("2");
        verify(contractRepository,times(1)).findByComboLegsDescriptionContains("3");
        verify(contractRepository,times(1)).findByComboLegsDescriptionContains("4");
        assertEquals(OptionalLong.of(90000000L), result);

    }
    @Test
    void testExistingContractsButNotMatching(){
        ContractDbo contractDBO = ContractDbo.builder().contractId(123).symbol(Symbol.SPX).id(90000000L).build();
        ContractDbo contractDbo2 = ContractDbo.builder().contractId(1234).symbol(Symbol.SPX).id(90000001L).build();
        ContractDbo contractDbo3 = ContractDbo.builder().contractId(12345).symbol(Symbol.SPX).id(90000002L).build();
        ContractDbo contractDbo4 = ContractDbo.builder().contractId(123456).symbol(Symbol.SPX).id(90000003L).build();
        when(contractRepository.findByComboLegsDescriptionContains("1")).thenReturn(new ArrayList<>(List.of(
                contractDBO, contractDbo3, contractDbo4)));
        when(contractRepository.findByComboLegsDescriptionContains("2")).thenReturn(new ArrayList<>(List.of(
                contractDbo2, contractDbo3)));
        when(contractRepository.findByComboLegsDescriptionContains("3")).thenReturn(new ArrayList<>(List.of(
                contractDBO, contractDbo2, contractDbo3, contractDbo4)));
        when(contractRepository.findByComboLegsDescriptionContains("4")).thenReturn(new ArrayList<>(List.of(
                contractDbo4, contractDbo2, contractDBO)));

        OptionalLong result = comboContractDataFinder.checkContractWithComboLegs(buildLegList());

        verify(contractRepository,times(1)).findByComboLegsDescriptionContains("1");
        verify(contractRepository,times(1)).findByComboLegsDescriptionContains("2");
        verify(contractRepository,times(1)).findByComboLegsDescriptionContains("3");
        verify(contractRepository,times(1)).findByComboLegsDescriptionContains("4");
        assertEquals(OptionalLong.empty(), result);
    }


}
