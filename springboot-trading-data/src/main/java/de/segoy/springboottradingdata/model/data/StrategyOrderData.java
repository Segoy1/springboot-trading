package de.segoy.springboottradingdata.model.data;

import de.segoy.springboottradingdata.model.Leg;
import de.segoy.springboottradingdata.model.data.entity.OrderDbo;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StrategyOrderData extends IBKRDataType {

    private OrderDbo orderData;
    private List<Leg> strategyLegs;
}
