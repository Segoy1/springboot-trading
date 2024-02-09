package de.segoy.springboottradingdata.model.data.entity;

import de.segoy.springboottradingdata.model.data.IBKRDataType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PositionData extends IBKRDataType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String account;

    @OneToOne(cascade = CascadeType.ALL)
    private ContractData contractData;
    private BigDecimal position;
    private double averageCost;

    private double totalCost;
}
