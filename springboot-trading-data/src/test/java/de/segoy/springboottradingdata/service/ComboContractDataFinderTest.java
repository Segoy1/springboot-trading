package de.segoy.springboottradingdata.service;

import com.ib.client.Types;
import de.segoy.springboottradingdata.model.data.entity.ComboLegData;
import de.segoy.springboottradingdata.model.data.entity.ContractData;
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
class ComboContractDataFinderTest {
    @Mock
    private ContractDataRepository contractDataRepository;
    @InjectMocks
    private ComboContractDataFinder comboContractDataFinder;



    List<ComboLegData> buildLegList() {

        final List<ComboLegData> legs = new ArrayList<>();
        legs.add(ComboLegData.builder().contractId(1).id(0L).ratio(1).action(Types.Action.BUY).build());
        legs.add(ComboLegData.builder().contractId(2).id(1L).ratio(2).action(Types.Action.SELL).build());
        legs.add(ComboLegData.builder().contractId(3).id(2L).ratio(1).action(Types.Action.SELL).build());
        legs.add(ComboLegData.builder().contractId(4).id(3L).ratio(3).action(Types.Action.BUY).build());
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
        ContractData contractData = ContractData.builder().contractId(123).symbol("SPX").id(90000000L).build();
        when(contractDataRepository.findByComboLegsDescriptionContains("1")).thenReturn(new ArrayList<>(List.of(contractData)));
        when(contractDataRepository.findByComboLegsDescriptionContains("2")).thenReturn(new ArrayList<>(List.of(contractData)));
        when(contractDataRepository.findByComboLegsDescriptionContains("3")).thenReturn(new ArrayList<>(List.of(contractData)));
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
        ContractData contractData = ContractData.builder().contractId(123).symbol("SPX").id(90000000L).build();
        ContractData contractData2 = ContractData.builder().contractId(1234).symbol("SPX").id(90000001L).build();
        ContractData contractData3 = ContractData.builder().contractId(12345).symbol("SPX").id(90000002L).build();
        ContractData contractData4 = ContractData.builder().contractId(123456).symbol("SPX").id(90000003L).build();
        when(contractDataRepository.findByComboLegsDescriptionContains("1")).thenReturn(new ArrayList<>(List.of(contractData,contractData3,contractData4)));
        when(contractDataRepository.findByComboLegsDescriptionContains("2")).thenReturn(new ArrayList<>(List.of(contractData,contractData3)));
        when(contractDataRepository.findByComboLegsDescriptionContains("3")).thenReturn(new ArrayList<>(List.of(contractData,contractData2,contractData4)));
        when(contractDataRepository.findByComboLegsDescriptionContains("4")).thenReturn(new ArrayList<>(List.of(contractData4,contractData3,contractData2,contractData)));

        OptionalLong result = comboContractDataFinder.checkContractWithComboLegs(buildLegList());

        verify(contractDataRepository,times(1)).findByComboLegsDescriptionContains("1");
        verify(contractDataRepository,times(1)).findByComboLegsDescriptionContains("2");
        verify(contractDataRepository,times(1)).findByComboLegsDescriptionContains("3");
        verify(contractDataRepository,times(1)).findByComboLegsDescriptionContains("4");
        assertEquals(OptionalLong.of(90000000L), result);

    }
    @Test
    void testExistingContractsButNotMatching(){
        ContractData contractData = ContractData.builder().contractId(123).symbol("SPX").id(90000000L).build();
        ContractData contractData2 = ContractData.builder().contractId(1234).symbol("SPX").id(90000001L).build();
        ContractData contractData3 = ContractData.builder().contractId(12345).symbol("SPX").id(90000002L).build();
        ContractData contractData4 = ContractData.builder().contractId(123456).symbol("SPX").id(90000003L).build();
        when(contractDataRepository.findByComboLegsDescriptionContains("1")).thenReturn(new ArrayList<>(List.of(contractData,contractData3,contractData4)));
        when(contractDataRepository.findByComboLegsDescriptionContains("2")).thenReturn(new ArrayList<>(List.of(contractData2,contractData3)));
        when(contractDataRepository.findByComboLegsDescriptionContains("3")).thenReturn(new ArrayList<>(List.of(contractData,contractData2,contractData3,contractData4)));
        when(contractDataRepository.findByComboLegsDescriptionContains("4")).thenReturn(new ArrayList<>(List.of(contractData4,contractData2,contractData)));

        OptionalLong result = comboContractDataFinder.checkContractWithComboLegs(buildLegList());

        verify(contractDataRepository,times(1)).findByComboLegsDescriptionContains("1");
        verify(contractDataRepository,times(1)).findByComboLegsDescriptionContains("2");
        verify(contractDataRepository,times(1)).findByComboLegsDescriptionContains("3");
        verify(contractDataRepository,times(1)).findByComboLegsDescriptionContains("4");
        assertEquals(OptionalLong.empty(), result);
    }


}
