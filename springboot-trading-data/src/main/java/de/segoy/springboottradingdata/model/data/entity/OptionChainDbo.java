package de.segoy.springboottradingdata.model.data.entity;

import de.segoy.springboottradingdata.model.data.IBKRDataType;
import de.segoy.springboottradingdata.model.subtype.Symbol;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "option_chain_dbo", schema = "trading")
public class OptionChainDbo extends IBKRDataType {

  @Id private Long id;
  @NotNull private Long lastTradeDate;
  @Enumerated(EnumType.STRING)
  @NotNull private Symbol symbol;
  @OneToOne private OptionListDbo calls;
  @OneToOne private OptionListDbo puts;
}
