package de.segoy.springboottradingdata.model.data.entity;

import com.ib.client.OrderStatus;
import com.ib.client.OrderType;
import com.ib.client.Types;
import de.segoy.springboottradingdata.model.data.IBKRDataType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderData extends IBKRDataType {

    @Id
    private Long id;

    @Enumerated(EnumType.STRING)
    private Types.Action action;

    private BigDecimal totalQuantity;

    @Enumerated(EnumType.STRING)
    private OrderType orderType;
    private BigDecimal limitPrice;
    private BigDecimal auctionPrice;
    @Enumerated(EnumType.STRING)
    private Types.TimeInForce timeInForce; // "Time in Force" - DAY, GTC, etc.
    // native cash quantity
    private BigDecimal cashQuantity;
    private Boolean usePriceManagementAlgorithm;

    @ManyToOne
    private ContractData contractData;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;
}
