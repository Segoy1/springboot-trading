package de.segoy.springboottradingdata.model.data.entity;

import de.segoy.springboottradingdata.model.data.IBKRDataType;
import de.segoy.springboottradingdata.model.data.kafka.ContractData;
import de.segoy.springboottradingdata.model.data.kafka.PositionData;
import jakarta.persistence.*;
import java.math.BigDecimal;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PositionDbo extends IBKRDataType {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String account;

  @OneToOne(cascade = CascadeType.ALL)
  private ContractDbo contractDBO;

  //total amount
  private BigDecimal position;
  private BigDecimal averageCost;

  private BigDecimal totalCost;

  public PositionData toKafkaPositionData() {
    ContractData contractData = contractDBO.toKafkaContractData();
    return PositionData.builder()
        .account(account)
        .contractData(contractData)
        .position(position)
        .averageCost(averageCost)
        .totalCost(totalCost)
        .build();
  }
}
