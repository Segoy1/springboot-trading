package de.segoy.springboottradingdata.service;

import com.ib.client.Types;
import de.segoy.springboottradingdata.model.data.entity.ComboLegDataDBO;
import de.segoy.springboottradingdata.model.data.entity.ContractDataDBO;
import de.segoy.springboottradingdata.model.subtype.Symbol;
import de.segoy.springboottradingdata.repository.ContractDataRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ComboContractDataDBOFinderTest {
    @Mock
    private ContractDataRepository contractDataRepository;
    @InjectMocks
    private ComboContractDataFinder comboContractDataFinder;



    List<ComboLegDataDBO> buildLegList() {

        final List<ComboLegDataDBO> legs = new ArrayList<>();
        legs.add(ComboLegDataDBO.builder().contractId(1).id(0L).ratio(1).action(Types.Action.BUY).build());
        legs.add(ComboLegDataDBO.builder().contractId(2).id(1L).ratio(2).action(Types.Action.SELL).build());
        legs.add(ComboLegDataDBO.builder().contractId(3).id(2L).ratio(1).action(Types.Action.SELL).build());
        legs.add(ComboLegDataDBO.builder().contractId(4).id(3L).ratio(3).action(Types.Action.BUY).build());
        return legs;
    }

    @Test
    void testNoExistingContractsForAllLegs() {
        when(contractDataRepository.findByComboLegsDescriptionContains("1")).thenReturn(new ArrayList<>());

        OptionalLong result = comboContractDataFinder.checkContractWithComboLegs(buildLegList());

        assertEquals(OptionalLong.empty(), result);

    }

    @Test
    void testExistingContractForFirstThreeLegs() {
        ContractDataDBO contractDataDBO = ContractDataDBO.builder().contractId(123).symbol(Symbol.SPX).id(90000000L).build();
        when(contractDataRepository.findByComboLegsDescriptionContains("1")).thenReturn(new ArrayList<>(List.of(
                contractDataDBO)));
        when(contractDataRepository.findByComboLegsDescriptionContains("2")).thenReturn(new ArrayList<>(List.of(
                contractDataDBO)));
        when(contractDataRepository.findByComboLegsDescriptionContains("3")).thenReturn(new ArrayList<>(List.of(
                contractDataDBO)));
        when(contractDataRepository.findByComboLegsDescriptionContains("4")).thenReturn(new ArrayList<>());

        OptionalLong result = comboContractDataFinder.checkContractWithComboLegs(buildLegList());

        verify(contractDataRepository,times(1)).findByComboLegsDescriptionContains("1");
        verify(contractDataRepository,times(1)).findByComboLegsDescriptionContains("2");
        verify(contractDataRepository,times(1)).findByComboLegsDescriptionContains("3");
        verify(contractDataRepository,times(1)).findByComboLegsDescriptionContains("4");
        assertEquals(OptionalLong.empty(), result);
    }

    @Test
    void testExisitingContractForAllLegs() {
        ContractDataDBO contractDataDBO = ContractDataDBO.builder().contractId(123).symbol(Symbol.SPX).id(90000000L).build();
        ContractDataDBO contractDataDBO2 = ContractDataDBO.builder().contractId(1234).symbol(Symbol.SPX).id(90000001L).build();
        ContractDataDBO contractDataDBO3 = ContractDataDBO.builder().contractId(12345).symbol(Symbol.SPX).id(90000002L).build();
        ContractDataDBO contractDataDBO4 = ContractDataDBO.builder().contractId(123456).symbol(Symbol.SPX).id(90000003L).build();
        when(contractDataRepository.findByComboLegsDescriptionContains("1")).thenReturn(new ArrayList<>(List.of(
                contractDataDBO, contractDataDBO3, contractDataDBO4)));
        when(contractDataRepository.findByComboLegsDescriptionContains("2")).thenReturn(new ArrayList<>(List.of(
                contractDataDBO, contractDataDBO3)));
        when(contractDataRepository.findByComboLegsDescriptionContains("3")).thenReturn(new ArrayList<>(List.of(
                contractDataDBO, contractDataDBO2, contractDataDBO4)));
        when(contractDataRepository.findByComboLegsDescriptionContains("4")).thenReturn(new ArrayList<>(List.of(
                contractDataDBO4, contractDataDBO3, contractDataDBO2, contractDataDBO)));

        OptionalLong result = comboContractDataFinder.checkContractWithComboLegs(buildLegList());

        verify(contractDataRepository,times(1)).findByComboLegsDescriptionContains("1");
        verify(contractDataRepository,times(1)).findByComboLegsDescriptionContains("2");
        verify(contractDataRepository,times(1)).findByComboLegsDescriptionContains("3");
        verify(contractDataRepository,times(1)).findByComboLegsDescriptionContains("4");
        assertEquals(OptionalLong.of(90000000L), result);

    }
    @Test
    void testExistingContractsButNotMatching(){
        ContractDataDBO contractDataDBO = ContractDataDBO.builder().contractId(123).symbol(Symbol.SPX).id(90000000L).build();
        ContractDataDBO contractDataDBO2 = ContractDataDBO.builder().contractId(1234).symbol(Symbol.SPX).id(90000001L).build();
        ContractDataDBO contractDataDBO3 = ContractDataDBO.builder().contractId(12345).symbol(Symbol.SPX).id(90000002L).build();
        ContractDataDBO contractDataDBO4 = ContractDataDBO.builder().contractId(123456).symbol(Symbol.SPX).id(90000003L).build();
        when(contractDataRepository.findByComboLegsDescriptionContains("1")).thenReturn(new ArrayList<>(List.of(
                contractDataDBO, contractDataDBO3, contractDataDBO4)));
        when(contractDataRepository.findByComboLegsDescriptionContains("2")).thenReturn(new ArrayList<>(List.of(
                contractDataDBO2, contractDataDBO3)));
        when(contractDataRepository.findByComboLegsDescriptionContains("3")).thenReturn(new ArrayList<>(List.of(
                contractDataDBO, contractDataDBO2, contractDataDBO3, contractDataDBO4)));
        when(contractDataRepository.findByComboLegsDescriptionContains("4")).thenReturn(new ArrayList<>(List.of(
                contractDataDBO4, contractDataDBO2, contractDataDBO)));

        OptionalLong result = comboContractDataFinder.checkContractWithComboLegs(buildLegList());

        verify(contractDataRepository,times(1)).findByComboLegsDescriptionContains("1");
        verify(contractDataRepository,times(1)).findByComboLegsDescriptionContains("2");
        verify(contractDataRepository,times(1)).findByComboLegsDescriptionContains("3");
        verify(contractDataRepository,times(1)).findByComboLegsDescriptionContains("4");
        assertEquals(OptionalLong.empty(), result);
    }


}
