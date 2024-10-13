package de.segoy.springboottradingdata.modelconverter;

import com.ib.client.ComboLeg;
import com.ib.client.Contract;
import com.ib.client.Types;
import de.segoy.springboottradingdata.model.data.entity.ContractDbo;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class IBKRContractToContractDboTest {


    private final IBKRToContractDbo ibkrToContractDbo = new IBKRToContractDbo();

    @Test
    void testConvertDataMinimumData() {
        Contract contract = new Contract();
        contract.right(Types.Right.Call);
        contract.symbol("SPX");
        contract.secType(Types.SecType.STK);
        contract.currency("USD");
        contract.exchange("SMART");

        ContractDbo contractDBO = ibkrToContractDbo.convertIBKRContract(contract);
        assertEquals(contract.right(), Types.Right.Call);
        assertEquals(contract.secType(), Types.SecType.STK);
        assertEquals("SPX", contractDBO.getSymbol().name());
        assertEquals("USD", contractDBO.getCurrency());
        assertEquals("SMART", contractDBO.getExchange());
//        assertTrue(contractData.isTouchedByApi());
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
        contract.symbol("SPX");
        contract.secType(Types.SecType.STK);
        contract.currency("USD");
        contract.exchange("SMART");
        contract.lastTradeDateOrContractMonth("20231117");
        contract.multiplier("100");
        contract.localSymbol("P BMW  20221216 72 M");
        contract.tradingClass("SPXW");
        contract.includeExpired(false);
        contract.comboLegsDescrip("description goes here");
        contract.strike(72);
        contract.comboLegs(legs);

        ContractDbo contractDBO = ibkrToContractDbo.convertIBKRContract(contract);
        assertEquals(contractDBO.getRight(), Types.Right.Call);
        assertEquals(contractDBO.getSecurityType(), Types.SecType.STK);
        assertEquals("SPX", contractDBO.getSymbol().name());
        assertEquals("USD", contractDBO.getCurrency());
        assertEquals("SMART", contractDBO.getExchange());
        assertEquals("20231117", contractDBO.getLastTradeDate());
        assertEquals("100", contractDBO.getMultiplier());
        assertEquals("P BMW  20221216 72 M", contractDBO.getLocalSymbol());
        assertEquals("SPXW", contractDBO.getTradingClass());
        assertFalse(contractDBO.isIncludeExpired());
        assertEquals(contractDBO.getStrike(), BigDecimal.valueOf(72.0 ));
        assertEquals(contractDBO.getComboLegs().get(0).getContractId(), 1);
        assertEquals(contractDBO.getComboLegs().get(0).getRatio(), 1);
        assertEquals(contractDBO.getComboLegs().get(0).getAction().toString(), "BUY");
        assertEquals(contractDBO.getComboLegs().get(0).getExchange(), "SMART");
        assertEquals(contractDBO.getContractId(), 123);
    }

}
