package de.segoy.springboottradingdata.modelconverter;

import com.ib.client.Contract;
import com.ib.client.Types;
import de.segoy.springboottradingdata.model.data.entity.ComboLegDbo;
import de.segoy.springboottradingdata.model.data.entity.ContractDbo;
import de.segoy.springboottradingdata.model.subtype.Symbol;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class ContractDboToIBKRContractTestDBO {

    private final ContractDboToIBKRContract contractDboToIBKRContract = new ContractDboToIBKRContract();

    @Test
    void testConvertDataMinimumData(){
        ContractDbo contractDBO = ContractDbo.builder()
                .right(Types.Right.Call)
                .symbol(Symbol.SPX)
                .securityType(Types.SecType.STK)
                .currency("USD")
                .exchange("SMART").build();

        Contract contract = contractDboToIBKRContract.convertContractData(contractDBO);
        assertEquals(contract.right(), Types.Right.Call);
        assertEquals(contract.secType(), Types.SecType.STK);
        assertEquals("SPX", contract.symbol());
        assertEquals("USD", contract.currency());
        assertEquals("SMART", contract.exchange());
    }

    @Test
    void testConvertAllData(){
        ComboLegDbo comboLegDBO = ComboLegDbo.builder()
                .contractId(1)
                .ratio(1)
                .action(Types.Action.BUY)
                .exchange("SMART")
                .build();

        ContractDbo contractDBO = ContractDbo.builder()
                .contractId(123)
                .right(Types.Right.Call)
                .symbol(Symbol.SPX)
                .securityType(Types.SecType.STK)
                .currency("USD")
                .exchange("SMART")
                .lastTradeDate("20231117")
                .multiplier("100")
                .localSymbol("P BMW  20221216 72 M")
                .tradingClass("SPXW")
                .includeExpired(false)
                .comboLegsDescription("description goes here")
                .strike(BigDecimal.valueOf(72))
                .comboLegs(List.of(comboLegDBO)).build();

        Contract contract = contractDboToIBKRContract.convertContractData(contractDBO);
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
