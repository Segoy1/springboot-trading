package de.segoy.springboottradingdata.modelconverter;

import com.ib.client.Contract;
import com.ib.client.Types;
import de.segoy.springboottradingdata.model.ComboLegData;
import de.segoy.springboottradingdata.model.ContractData;
import de.segoy.springboottradingdata.type.Currency;
import de.segoy.springboottradingdata.type.Symbol;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class ContractDataToIBKRContractTest {

    private ContractDataToIBKRContract contractDataToIBKRContract = new ContractDataToIBKRContract();

    @Test
    void testConvertDataMinimumData(){
        ContractData contractData = ContractData.builder()
                .right(Types.Right.Call)
                .symbol(Symbol.SPX.toString())
                .securityType(Types.SecType.STK)
                .currency(Currency.USD.toString())
                .exchange("SMART").build();

        Contract contract = contractDataToIBKRContract.convertContractData(contractData);
        assertEquals(contract.right(), Types.Right.Call);
        assertEquals(contract.secType(), Types.SecType.STK);
        assertEquals("SPX", contract.symbol());
        assertEquals("USD", contract.currency());
        assertEquals("SMART", contract.exchange());
    }

    @Test
    void testConvertAllData(){
        ComboLegData comboLegData = ComboLegData.builder()
                .contractId(1)
                .ratio(1)
                .action(Types.Action.BUY)
                .exchange("SMART")
                .build();

        ContractData contractData = ContractData.builder()
                .contractId(123)
                .right(Types.Right.Call)
                .symbol(Symbol.SPX.toString())
                .securityType(Types.SecType.STK)
                .currency(Currency.USD.toString())
                .exchange("SMART")
                .lastTradeDate("20231117")
                .multiplier("100")
                .localSymbol("P BMW  20221216 72 M")
                .tradingClass("SPXW")
                .includeExpired(false)
                .comboLegsDescription("description goes here")
                .strike(BigDecimal.valueOf(72))
                .comboLeg(comboLegData).build();

        Contract contract = contractDataToIBKRContract.convertContractData(contractData);
        assertEquals(contract.right(), Types.Right.Call);
        assertEquals(contract.secType(), Types.SecType.STK);
        assertEquals("SPX", contract.symbol());
        assertEquals("USD", contract.currency());
        assertEquals("SMART", contract.exchange());
        assertEquals("20231117", contract.lastTradeDateOrContractMonth());
        assertEquals("100", contract.multiplier());
        assertEquals("P BMW  20221216 72 M", contract.localSymbol());
        assertEquals("SPXW", contract.tradingClass());
        assertFalse(contract.includeExpired());
        assertEquals(contract.strike(),72);
        assertEquals(contract.comboLegs().get(0).conid(),1);
        assertEquals(contract.comboLegs().get(0).ratio(),1);
        assertEquals(contract.comboLegs().get(0).action().toString(),"BUY");
        assertEquals(contract.comboLegs().get(0).exchange(),"SMART");
        assertEquals(contract.conid(), 123);
    }

}