package de.segoy.springboottradingdata.model.data.kafka;

import com.ib.client.OrderStatus;
import com.ib.client.OrderType;
import com.ib.client.Types;
import java.math.BigDecimal;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class KafkaOrderData extends KafkaDataType{

    private Long id;

    
    private Types.Action action;

    private BigDecimal totalQuantity;
    
    private OrderType orderType;
    private BigDecimal limitPrice;
    private BigDecimal auctionPrice;
    private Types.TimeInForce timeInForce; // "Time in Force" - DAY, GTC, etc.
    // native cash quantity
    private BigDecimal cashQuantity;
    private Boolean usePriceManagementAlgorithm;
    
    private KafkaContractData contractData;
    private OrderStatus status;
}
