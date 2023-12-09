package de.segoy.springboottradingdata.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HistoricalMarketData extends IBKRDataTypeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //Maybe Have to refactor to long
    private Long id;

    private Integer contractId;

    private Timestamp time;
    private double open;
    private double high;
    private double low;
    private double close;
    private BigDecimal volume;
    private Integer count;
    private BigDecimal wap;
}
