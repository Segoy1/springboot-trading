package de.segoy.springboottradingdata.model.data.entity;

import de.segoy.springboottradingdata.model.data.IBKRDataType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LastPriceLiveMarketDataDbo extends IBKRDataType {

    @Id
    private Long tickerId;

    @OneToOne
    private ContractDbo contractDBO;

    private double lastPrice;
    private double bidPrice;
    private double askPrice;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
}