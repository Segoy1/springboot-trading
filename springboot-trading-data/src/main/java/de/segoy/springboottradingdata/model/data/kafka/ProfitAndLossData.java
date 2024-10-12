package de.segoy.springboottradingdata.model.data.kafka;

import de.segoy.springboottradingdata.model.data.IBKRDataType;
import jakarta.persistence.Id;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProfitAndLossData extends IBKRDataType {

    @Id
    private Long id;

    private BigDecimal pos;
    private Double dailyPnL;
    private Double unrealizedPnL;
    private Double realizedPnL;
    private Double currentValue;
}
