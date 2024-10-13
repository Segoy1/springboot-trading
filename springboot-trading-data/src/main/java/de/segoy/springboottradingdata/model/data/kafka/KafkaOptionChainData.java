package de.segoy.springboottradingdata.model.data.kafka;

import de.segoy.springboottradingdata.model.subtype.Symbol;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class KafkaOptionChainData extends KafkaDataType {

  private Long lastTradeDate;
  private Symbol symbol;
  private KafkaOptionListData calls;
  private KafkaOptionListData puts;
  
}
