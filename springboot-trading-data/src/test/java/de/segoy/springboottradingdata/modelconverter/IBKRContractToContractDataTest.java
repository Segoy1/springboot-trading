package de.segoy.springboottradingdata.modelconverter;

import com.ib.client.ComboLeg;
import com.ib.client.Contract;
import com.ib.client.Types;
import de.segoy.springboottradingdata.model.ContractData;
import de.segoy.springboottradingdata.type.Currency;
import de.segoy.springboottradingdata.type.Symbol;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class IBKRContractToContractDataTest {


    private IBKRContractToContractData ibkrContractToContractData = new IBKRContractToContractData();

    @Test
    void testConvertDataMinimumData() {
        Contract contract = new Contract();
        contract.right(Types.Right.Call);
        contract.symbol(Symbol.SPX.toString());
        contract.secType(Types.SecType.STK);
        contract.currency(Currency.USD.toString());
        contract.exchange("SMART");

        ContractData contractData = ibkrContractToContractData.convertIBKRContract(contract);
        assertEquals(contract.right(), Types.Right.Call);
        assertEquals(contract.secType(), Types.SecType.STK);
        assertEquals("SPX", contract.symbol());
        assertEquals("USD", contract.currency());
        assertEquals("SMART", contract.exchange());
    }

    @Test
    void testConvertAllData() {
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

        ContractData contractData = ibkrContractToContractData.convertIBKRContract(contract);
        assertEquals(contractData.getRight(), Types.Right.Call);
        assertEquals(contractData.getSecurityType(), Types.SecType.STK);
        assertEquals("SPX", contractData.getSymbol());
        assertEquals("USD", contractData.getCurrency());
        assertEquals("SMART", contractData.getExchange());
        assertEquals("20231117", contractData.getLastTradeDate());
        assertEquals("100", contractData.getMultiplier());
        assertEquals("P BMW  20221216 72 M", contractData.getLocalSymbol());
        assertEquals("SPXW", contractData.getTradingClass());
        assertFalse(contractData.isIncludeExpired());
        assertEquals(contractData.getStrike(), BigDecimal.valueOf(72.0 ));
        assertEquals(contractData.getComboLegs().get(0).getContractId(), 1);
        assertEquals(contractData.getComboLegs().get(0).getRatio(), 1);
        assertEquals(contractData.getComboLegs().get(0).getAction().toString(), "BUY");
        assertEquals(contractData.getComboLegs().get(0).getExchange(), "SMART");
        assertEquals(contractData.getContractId(), 123);
    }

}