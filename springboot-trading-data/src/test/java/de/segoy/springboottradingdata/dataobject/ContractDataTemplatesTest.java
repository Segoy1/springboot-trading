package de.segoy.springboottradingdata.dataobject;

import de.segoy.springboottradingdata.model.data.entity.ContractData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ContractDataTemplatesTest {

    @Test
    void testSpxData(){
        ContractData contractData = ContractDataTemplates.SpxData();

        assertEquals("IND",contractData.getSecurityType().getApiString());
        assertEquals("SPX",contractData.getSymbol().name());
        assertEquals("USD",contractData.getCurrency());
        assertEquals("CBOE",contractData.getExchange());
    }

    @Test
    void testSpxOptionData(){
        ContractData contractData = ContractDataTemplates.SpxOptionData();

        assertEquals("IND",contractData.getSecurityType().getApiString());
        assertEquals("SPX",contractData.getSymbol().name());
        assertEquals("USD",contractData.getCurrency());
        assertEquals("OPRA",contractData.getExchange());
    }

}
