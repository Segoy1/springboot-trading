package de.segoy.springboottradingdata.dataobject;

import com.ib.client.Types;
import de.segoy.springboottradingdata.config.TradingConstants;
import de.segoy.springboottradingdata.model.data.entity.ContractDbo;
import de.segoy.springboottradingdata.model.subtype.Symbol;

public class ContractDataTemplates {

  public static ContractDbo SpxData() {
    return ContractDbo.builder()
        .symbol(Symbol.SPX)
        .securityType(Types.SecType.IND)
        .currency(TradingConstants.USD)
        .exchange("CBOE")
        .build();
  }

  public static ContractDbo NasDaqData() {
    return ContractDbo.builder()
        .symbol(Symbol.NDX)
        .securityType(Types.SecType.IND)
        .currency(TradingConstants.USD)
        .exchange("NASDAQ")
        .build();
  }

  public static ContractDbo XOIData() {
    return ContractDbo.builder()
        .symbol(Symbol.XOI)
        .securityType(Types.SecType.IND)
        .currency(TradingConstants.USD)
        .exchange("PSE")
        .build();
  }

  public static ContractDbo SpxOptionData() {
    return ContractDbo.builder()
        .symbol(Symbol.SPX)
        .securityType(Types.SecType.IND)
        .currency(TradingConstants.USD)
        .exchange("OPRA")
        .build();
  }

  public static ContractDbo VIXData() {
    return ContractDbo.builder()
        .symbol(Symbol.VIX)
        .securityType(Types.SecType.IND)
        .currency(TradingConstants.USD)
        .exchange("CBOE")
        .build();
  }

  public static ContractDbo TLTData() {
    return ContractDbo.builder()
        .symbol(Symbol.TLT)
        .securityType(Types.SecType.STK)
        .currency(TradingConstants.USD)
        .exchange("NASDAQ")
        .build();
  }

  public static ContractDbo GCData() {
    return ContractDbo.builder()
        .symbol(Symbol.GC)
        .securityType(Types.SecType.IND)
        .currency(TradingConstants.USD)
        .exchange("COMEX")
        .build();
  }

  public static ContractDbo CLData() {
    return ContractDbo.builder()
        .symbol(Symbol.CL)
        .securityType(Types.SecType.IND)
        .currency(TradingConstants.USD)
        .exchange("NYMEX")
        .build();
  }
  public static ContractDbo SPXWComboData(){
    return ContractDbo.builder()
            .symbol(Symbol.SPX)
            .securityType(Types.SecType.BAG)
            .currency(TradingConstants.USD)
            .exchange(TradingConstants.CBOE)
            .tradingClass(Symbol.SPXW.name())
            .build();

  }
}
