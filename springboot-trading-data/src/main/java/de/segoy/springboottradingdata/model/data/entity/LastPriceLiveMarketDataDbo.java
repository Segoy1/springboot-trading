package de.segoy.springboottradingdata.model.data.entity;

import de.segoy.springboottradingdata.model.data.IBKRDataType;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "last_price_live_market_data_dbo", schema = "trading")
public class LastPriceLiveMarketDataDbo extends IBKRDataType {

    @Id
    private Long tickerId;

    @OneToOne
    private ContractDbo contractDBO;

    private Double lastPrice;
    private Double bidPrice;
    private Double askPrice;

    @UpdateTimestamp
    private LocalDateTime lastModifiedDate;
}
