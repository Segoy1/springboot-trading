package de.segoy.springboottradingdata.dataobject;

import com.ib.client.Types;
import de.segoy.springboottradingdata.config.TradingConstants;
import de.segoy.springboottradingdata.model.data.entity.ContractDataDBO;
import de.segoy.springboottradingdata.model.subtype.Symbol;

public class ContractDataTemplates {

  public static ContractDataDBO SpxData() {
    return ContractDataDBO.builder()
        .symbol(Symbol.SPX)
        .securityType(Types.SecType.IND)
        .currency(TradingConstants.USD)
        .exchange("CBOE")
        .build();
  }

  public static ContractDataDBO NasDaqData() {
    return ContractDataDBO.builder()
        .symbol(Symbol.NDX)
        .securityType(Types.SecType.IND)
        .currency(TradingConstants.USD)
        .exchange("NASDAQ")
        .build();
  }

  public static ContractDataDBO XOIData() {
    return ContractDataDBO.builder()
        .symbol(Symbol.XOI)
        .securityType(Types.SecType.IND)
        .currency(TradingConstants.USD)
        .exchange("PSE")
        .build();
  }

  public static ContractDataDBO SpxOptionData() {
    return ContractDataDBO.builder()
        .symbol(Symbol.SPX)
        .securityType(Types.SecType.IND)
        .currency(TradingConstants.USD)
        .exchange("OPRA")
        .build();
  }

  public static ContractDataDBO VIXData() {
    return ContractDataDBO.builder()
        .symbol(Symbol.VIX)
        .securityType(Types.SecType.IND)
        .currency(TradingConstants.USD)
        .exchange("CBOE")
        .build();
  }

  public static ContractDataDBO TLTData() {
    return ContractDataDBO.builder()
        .symbol(Symbol.TLT)
        .securityType(Types.SecType.STK)
        .currency(TradingConstants.USD)
        .exchange("NASDAQ")
        .build();
  }

  public static ContractDataDBO GCData() {
    return ContractDataDBO.builder()
        .symbol(Symbol.GC)
        .securityType(Types.SecType.IND)
        .currency(TradingConstants.USD)
        .exchange("COMEX")
        .build();
  }

  public static ContractDataDBO CLData() {
    return ContractDataDBO.builder()
        .symbol(Symbol.CL)
        .securityType(Types.SecType.IND)
        .currency(TradingConstants.USD)
        .exchange("NYMEX")
        .build();
  }
  public static ContractDataDBO SPXWComboData(){
    return ContractDataDBO.builder()
            .symbol(Symbol.SPX)
            .securityType(Types.SecType.BAG)
            .currency(TradingConstants.USD)
            .exchange(TradingConstants.CBOE)
            .tradingClass(Symbol.SPXW.name())
            .build();

  }
}
