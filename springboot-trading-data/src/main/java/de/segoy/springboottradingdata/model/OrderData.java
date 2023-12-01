package de.segoy.springboottradingdata.model;

import com.ib.client.OrderType;
import com.ib.client.Types;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderData {

    @Id
    private Integer orderId;

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

    private boolean placed;
    private boolean executed;
}
