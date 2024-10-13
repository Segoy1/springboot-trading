package de.segoy.springboottradingdata.dataobject;

import de.segoy.springboottradingdata.model.data.entity.ContractDbo;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ContractDboTemplatesTest {

    @Test
    void testSpxData(){
        ContractDbo contractDBO = ContractDataTemplates.SpxData();

        assertEquals("IND", contractDBO.getSecurityType().getApiString());
        assertEquals("SPX", contractDBO.getSymbol().name());
        assertEquals("USD", contractDBO.getCurrency());
        assertEquals("CBOE", contractDBO.getExchange());
    }

    @Test
    void testSpxOptionData(){
        ContractDbo contractDBO = ContractDataTemplates.SpxOptionData();

        assertEquals("IND", contractDBO.getSecurityType().getApiString());
        assertEquals("SPX", contractDBO.getSymbol().name());
        assertEquals("USD", contractDBO.getCurrency());
        assertEquals("OPRA", contractDBO.getExchange());
    }

}
