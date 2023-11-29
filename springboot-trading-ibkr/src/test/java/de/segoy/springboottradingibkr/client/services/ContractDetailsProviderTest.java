package de.segoy.springboottradingibkr.client.services;

import com.ib.client.ComboLeg;
import com.ib.client.Contract;
import com.ib.client.Types;
import de.segoy.springboottradingdata.model.ContractData;
import de.segoy.springboottradingdata.modelconverter.IBKRContractToContractData;
import de.segoy.springboottradingdata.repository.ContractDataRepository;
import de.segoy.springboottradingdata.type.Currency;
import de.segoy.springboottradingdata.type.Symbol;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContractDetailsProviderTest {

    @Mock
    private IBKRContractToContractData ibkrContractToContractData;
    @Mock
    private ContractDataRepository contractDataRepository;
    @InjectMocks
    private ContractDetailsProvider contractDetailsProvider;

    @Test
    void testaddContractDetailsFromAPIToContractData(){
        Contract contract = buildContract();
        ContractData data = ContractData.builder().contractId(123).build();

        when(contractDataRepository.findAllByContractId(123)).thenReturn(new ArrayList<ContractData>());
        when(ibkrContractToContractData.convertIBKRContract(contract)).thenReturn(data);

        contractDetailsProvider.addContractDetailsFromAPIToContractData(1, contract);
        verify(contractDataRepository,times(1)).save(data);
        assertEquals(1, data.getId());
        assertTrue(data.isTouchedByApi());
    }

    @Test
    void testaddContractDetailsFromAPIToContractDataInvalidCall(){
        Contract contract = buildContract();
        ContractData data = ContractData.builder().contractId(123).build();

        ArrayList<ContractData> t = new ArrayList<>();
        t.add(ContractData.builder().build());
        when(contractDataRepository.findAllByContractId(123)).thenReturn(t);

        contractDetailsProvider.addContractDetailsFromAPIToContractData(1, contract);
        verify(contractDataRepository,times(0)).save(data);
        assertFalse(data.isTouchedByApi());
    }
    private Contract buildContract(){
        List<ComboLeg> legs = new ArrayList<>();
        ComboLeg comboLeg = new ComboLeg();
        comboLeg.conid(1);
        comboLeg.ratio(1);
        comboLeg.action(Types.Action.BUY);
        comboLeg.exchange("SMART");
        legs.add(comboLeg);

        Contract contract = new Contract();
        contract.conid(123);
        contract.right(Types.Right.Call);
        contract.symbol(Symbol.SPX.toString());
        contract.secType(Types.SecType.STK);
        contract.currency(Currency.USD.toString());
        contract.exchange("SMART");
        contract.lastTradeDateOrContractMonth("20231117");
        contract.multiplier("100");
        contract.localSymbol("P BMW  20221216 72 M");
        contract.tradingClass("SPXW");
        contract.includeExpired(false);
        contract.comboLegsDescrip("description goes here");
        contract.strike(72);
        contract.comboLegs(legs);
        return contract;
    }

}