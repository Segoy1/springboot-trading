package de.segoy.springboottradingdata.model.data.kafka;

import java.math.BigDecimal;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KafkaPositionData extends KafkaDataType{

    private String account;

    private KafkaContractData contractData;
    private BigDecimal position;
    private double averageCost;

    private double totalCost;
}
