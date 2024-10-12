package de.segoy.springboottradingdata.model.data.kafka;

import com.ib.client.Types;
import de.segoy.springboottradingdata.model.data.entity.ComboLegDataDBO;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KafkaComboLegData extends KafkaDataType {
  private Integer contractId;
  private Integer ratio;
  private Types.Action action; // BUY/SELL/SSHORT/SSHORTX
  private String exchange;

  @Transactional
  public ComboLegDataDBO toDBComboLegData() {
    return ComboLegDataDBO.builder()
        .contractId(contractId)
        .ratio(ratio)
        .action(action)
        .exchange(exchange)
        .build();
  }
}