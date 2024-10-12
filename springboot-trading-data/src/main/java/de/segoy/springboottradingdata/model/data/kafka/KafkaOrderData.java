package de.segoy.springboottradingdata.model.data.kafka;

import com.ib.client.OrderStatus;
import com.ib.client.OrderType;
import com.ib.client.Types;
import de.segoy.springboottradingdata.model.data.entity.ContractDataDBO;
import de.segoy.springboottradingdata.model.data.entity.OrderDataDBO;
import lombok.*;

import java.math.BigDecimal;


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

    public OrderDataDBO toKOrderDataDBO(){
        ContractDataDBO contract = contractData.toContractDataDBO();
        return OrderDataDBO.builder()
                .id(id)
                .action(action)
                .totalQuantity(totalQuantity)
                .orderType(orderType)
                .limitPrice(limitPrice)
                .auctionPrice(auctionPrice)
                .timeInForce(timeInForce)
                .cashQuantity(cashQuantity)
                .usePriceManagementAlgorithm(usePriceManagementAlgorithm)
                .contractDataDBO(contract)
                .status(status)
                .build();
    }
}
