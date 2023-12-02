package de.segoy.springboottradingdata.model;

import com.ib.client.OrderStatus;
import com.ib.client.OrderType;
import com.ib.client.Types;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderData extends BaseEntity{

    @Id
    private Integer id;

    @Enumerated(EnumType.STRING)
    private Types.Action action;

    private BigDecimal totalQuantity;

    @Enumerated(EnumType.STRING)
    private OrderType orderType;
    private BigDecimal limitPrice;
    private BigDecimal auctionPrice;
    private String timeInForce; // "Time in Force" - DAY, GTC, etc.
    // native cash quantity
    private BigDecimal cashQuantity;
    private Boolean usePriceManagementAlgorithm;

    @OneToOne
    @NotNull
    private ContractData contractData;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;
}
