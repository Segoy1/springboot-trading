package de.segoy.springboottradingdata.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.util.Date;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HistoricalData extends IBKRDataTypeEntity{

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

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date")
    private Date createDate;
}
