package de.segoy.springboottradingdata.model.data.kafka;

import com.ib.client.Types;
import de.segoy.springboottradingdata.model.data.entity.ComboLegDataDBO;
import de.segoy.springboottradingdata.model.data.entity.ContractDataDBO;
import de.segoy.springboottradingdata.model.subtype.Symbol;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KafkaContractData extends KafkaDataType {

  private Integer contractId; // ContractId From Call to IBKR Api
  @NotNull private Symbol symbol; // SPX
  @NotNull private Types.SecType securityType; // OPT
  @NotNull private String currency; // USD
  private String exchange; // "SMART"
  private String lastTradeDate; // format 20231116
  private BigDecimal strike; // 4100

  @Column(name = "trade_right")
  private Types.Right right; // "C" "P"

  private String multiplier; // "100"
  private String
      localSymbol; // can be used to define options Maybe best solution "P SPX  20231116 4100 W"
  private String tradingClass; // SPXW
  private boolean includeExpired; // can not be set to true for orders
  private String comboLegsDescription; // received in open order version 14 and up for all combos

  private List<KafkaComboLegData> comboLegs;

  @Transactional
  public ContractDataDBO toContractDataDBO() {
    List<ComboLegDataDBO> legs = new ArrayList<>();
    if (comboLegs != null) {
      comboLegs.forEach((leg) -> legs.add(leg.toDBComboLegData()));
    }
    return ContractDataDBO.builder()
        .contractId(contractId)
        .symbol(symbol)
        .securityType(securityType)
        .currency(currency)
        .exchange(exchange)
        .lastTradeDate(lastTradeDate)
        .strike(strike)
        .right(right)
        .multiplier(multiplier)
        .localSymbol(localSymbol)
        .tradingClass(tradingClass)
        .includeExpired(includeExpired)
        .comboLegsDescription(comboLegsDescription)
        .comboLegs(legs)
        .build();
  }
}
