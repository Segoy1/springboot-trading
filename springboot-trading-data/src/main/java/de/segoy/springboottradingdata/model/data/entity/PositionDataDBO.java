package de.segoy.springboottradingdata.model.data.entity;

import de.segoy.springboottradingdata.model.data.IBKRDataType;
import de.segoy.springboottradingdata.model.data.kafka.KafkaContractData;
import de.segoy.springboottradingdata.model.data.kafka.KafkaPositionData;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PositionDataDBO extends IBKRDataType {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String account;

  @OneToOne(cascade = CascadeType.ALL)
  private ContractDataDBO contractDataDBO;

  private BigDecimal position;
  private double averageCost;

  private double totalCost;

  public KafkaPositionData toKafkaPositionData() {
    KafkaContractData kafkaContractData = contractDataDBO.toKafkaContractData();
    return KafkaPositionData.builder()
        .account(account)
        .contractData(kafkaContractData)
        .position(position)
        .averageCost(averageCost)
        .totalCost(totalCost)
        .build();
  }
}
