package de.segoy.springboottradingdata.model.data.kafka;

import java.math.BigDecimal;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PositionData extends KafkaDataType{

    private String account;

    private ContractData contractData;
    private BigDecimal position;
    private BigDecimal averageCost;

    private BigDecimal totalCost;
}
