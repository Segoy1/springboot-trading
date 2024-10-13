package de.segoy.springboottradingdata.model.data.entity;

import com.ib.client.Types;
import de.segoy.springboottradingdata.model.data.kafka.KafkaComboLegData;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ComboLegDbo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer contractId;
    private Integer ratio;

    @Enumerated(EnumType.STRING)
    private Types.Action action; // BUY/SELL/SSHORT/SSHORTX
    private String exchange;

    public KafkaComboLegData toKafkaComboLegData() {
        return KafkaComboLegData.builder()
                .contractId(contractId)
                .ratio(ratio)
                .action(action)
                .exchange(exchange)
                .build();
    }
}
