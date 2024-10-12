package de.segoy.springboottradingdata.dataobject;

import de.segoy.springboottradingdata.model.data.entity.ContractDataDBO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ContractDataDBOTemplatesTest {

    @Test
    void testSpxData(){
        ContractDataDBO contractDataDBO = ContractDataTemplates.SpxData();

        assertEquals("IND", contractDataDBO.getSecurityType().getApiString());
        assertEquals("SPX", contractDataDBO.getSymbol().name());
        assertEquals("USD", contractDataDBO.getCurrency());
        assertEquals("CBOE", contractDataDBO.getExchange());
    }

    @Test
    void testSpxOptionData(){
        ContractDataDBO contractDataDBO = ContractDataTemplates.SpxOptionData();

        assertEquals("IND", contractDataDBO.getSecurityType().getApiString());
        assertEquals("SPX", contractDataDBO.getSymbol().name());
        assertEquals("USD", contractDataDBO.getCurrency());
        assertEquals("OPRA", contractDataDBO.getExchange());
    }

}
