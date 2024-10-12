package de.segoy.springboottradingdata.modelconverter;

import com.ib.client.ComboLeg;
import com.ib.client.Contract;
import com.ib.client.Types;
import de.segoy.springboottradingdata.model.data.entity.ContractDataDBO;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class IBKRContractToContractDataDBOTest {


    private final IBKRContractToContractData ibkrContractToContractData = new IBKRContractToContractData();

    @Test
    void testConvertDataMinimumData() {
        Contract contract = new Contract();
        contract.right(Types.Right.Call);
        contract.symbol("SPX");
        contract.secType(Types.SecType.STK);
        contract.currency("USD");
        contract.exchange("SMART");

        ContractDataDBO contractDataDBO = ibkrContractToContractData.convertIBKRContract(contract);
        assertEquals(contract.right(), Types.Right.Call);
        assertEquals(contract.secType(), Types.SecType.STK);
        assertEquals("SPX", contractDataDBO.getSymbol().name());
        assertEquals("USD", contractDataDBO.getCurrency());
        assertEquals("SMART", contractDataDBO.getExchange());
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

        ContractDataDBO contractDataDBO = ibkrContractToContractData.convertIBKRContract(contract);
        assertEquals(contractDataDBO.getRight(), Types.Right.Call);
        assertEquals(contractDataDBO.getSecurityType(), Types.SecType.STK);
        assertEquals("SPX", contractDataDBO.getSymbol().name());
        assertEquals("USD", contractDataDBO.getCurrency());
        assertEquals("SMART", contractDataDBO.getExchange());
        assertEquals("20231117", contractDataDBO.getLastTradeDate());
        assertEquals("100", contractDataDBO.getMultiplier());
        assertEquals("P BMW  20221216 72 M", contractDataDBO.getLocalSymbol());
        assertEquals("SPXW", contractDataDBO.getTradingClass());
        assertFalse(contractDataDBO.isIncludeExpired());
        assertEquals(contractDataDBO.getStrike(), BigDecimal.valueOf(72.0 ));
        assertEquals(contractDataDBO.getComboLegs().get(0).getContractId(), 1);
        assertEquals(contractDataDBO.getComboLegs().get(0).getRatio(), 1);
        assertEquals(contractDataDBO.getComboLegs().get(0).getAction().toString(), "BUY");
        assertEquals(contractDataDBO.getComboLegs().get(0).getExchange(), "SMART");
        assertEquals(contractDataDBO.getContractId(), 123);
    }

}
