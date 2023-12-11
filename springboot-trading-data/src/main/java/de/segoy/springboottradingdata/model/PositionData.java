package de.segoy.springboottradingdata.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PositionData extends IBKRDataTypeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String account;

    @OneToOne
    private ContractData contractData;
    private BigDecimal position;
    private double averageCost;
}
