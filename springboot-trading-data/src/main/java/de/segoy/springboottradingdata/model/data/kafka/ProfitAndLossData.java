package de.segoy.springboottradingdata.model.data.kafka;

import jakarta.persistence.Id;
import java.math.BigDecimal;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProfitAndLossData extends KafkaDataType {

    @Id
    private Long id;

    private BigDecimal pos;
    private Double dailyPnL;
    private Double unrealizedPnL;
    private Double realizedPnL;
    private Double currentValue;
}
