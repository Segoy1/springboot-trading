package de.segoy.springboottradingdata.dataobject;

import de.segoy.springboottradingdata.model.ContractData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ContractDataObjectsTest {

    @Test
    void testSpxData(){
        ContractData contractData = ContractDataObjects.SpxData();

        assertEquals("IND",contractData.getSecurityType().getApiString());
        assertEquals("SPX",contractData.getSymbol());
        assertEquals("USD",contractData.getCurrency());
        assertEquals("CBOE",contractData.getExchange());
    }

    @Test
    void testSpxOptionData(){
        ContractData contractData = ContractDataObjects.SpxOptionData();

        assertEquals("IND",contractData.getSecurityType().getApiString());
        assertEquals("SPX",contractData.getSymbol());
        assertEquals("USD",contractData.getCurrency());
        assertEquals("OPRA",contractData.getExchange());
    }

}