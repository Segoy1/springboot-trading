package de.segoy.springboottradingdata.dataobject;

import com.ib.client.Types;
import de.segoy.springboottradingdata.model.entity.ContractData;

public class ContractDataTemplates {

    public static ContractData SpxData(){
        return ContractData.builder()
                .symbol("SPX")
                .securityType(Types.SecType.IND)
                .currency("USD")
                .exchange("CBOE")
                .build();
    }
    public static ContractData SpxOptionData(){
        return ContractData.builder()
                .symbol("SPX")
                .securityType(Types.SecType.IND)
                .currency("USD")
                .exchange("OPRA")
                .build();
    }
}
