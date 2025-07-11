package de.segoy.springboottradingdata.model.data.entity;

import de.segoy.springboottradingdata.model.data.IBKRDataType;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "historical_dbo", schema = "trading")
public class HistoricalDbo extends IBKRDataType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer contractId;

    private Timestamp time;
    private double open;
    private double high;
    private double low;
    private double close;
    private BigDecimal volume;

    //number of trades
    private Integer count;

    //weighted average price
    private BigDecimal wap;

    @UpdateTimestamp
    private LocalDateTime lastModifiedDate;
}
