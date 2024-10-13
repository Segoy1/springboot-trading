package de.segoy.springboottradingdata.model.data.entity;

import de.segoy.springboottradingdata.model.data.IBKRDataType;
import de.segoy.springboottradingdata.model.subtype.Symbol;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OptionChainDataDBO extends IBKRDataType {

  @Id private Long lastTradeDate;
  @NotNull private Symbol symbol;
  @OneToOne private OptionListDBO calls;
  @OneToOne private OptionListDBO puts;
}
