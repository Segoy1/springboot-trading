package de.segoy.springboottradingdata.dataobject;

import com.ib.client.Types;
import de.segoy.springboottradingdata.model.data.entity.ContractData;

public class ContractDataTemplates {

    public static ContractData SpxData(){
        return ContractData.builder()
                .symbol("SPX")
                .securityType(Types.SecType.IND)
                .currency("USD")
                .exchange("CBOE")
                .build();
    }
    public static ContractData NasDaqData(){
        return ContractData.builder()
                .symbol("NDX")
                .securityType(Types.SecType.IND)
                .currency("USD")
                .exchange("NASDAQ")
                .build();
    }
    public static ContractData XOIData(){
        return ContractData.builder()
                .symbol("XOI")
                .securityType(Types.SecType.IND)
                .currency("USD")
                .exchange("PSE")
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
    public static ContractData VIXData(){
        return ContractData.builder()
                .symbol("VIX")
                .securityType(Types.SecType.IND)
                .currency("USD")
                .exchange("CBOE")
                .build();
    }
    public static ContractData TLTData(){
        return ContractData.builder()
                .symbol("TLT")
                .securityType(Types.SecType.STK)
                .currency("USD")
                .exchange("NASDAQ")
                .build();
    }
    public static ContractData GCData(){
        return ContractData.builder()
                .symbol("GC")
                .securityType(Types.SecType.IND)
                .currency("USD")
                .exchange("COMEX")
                .build();
    }
    public static ContractData CLData(){
        return ContractData.builder()
                .symbol("CL")
                .securityType(Types.SecType.IND)
                .currency("USD")
                .exchange("NYMEX")
                .build();
    }
}
