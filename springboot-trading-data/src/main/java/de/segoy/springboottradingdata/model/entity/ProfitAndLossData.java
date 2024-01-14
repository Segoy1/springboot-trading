package de.segoy.springboottradingdata.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProfitAndLossData extends IBKRDataTypeEntity{

    @Id
    private Long id;

    private BigDecimal pos;
    private Double dailyPnL;
    private Double unrealizedPnL;
    private Double realizedPnL;
    private Double value;
}
