package de.segoy.springboottradingdata.model.data;

import de.segoy.springboottradingdata.model.Leg;
import de.segoy.springboottradingdata.model.data.entity.OrderData;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StrategyOrderData extends IBKRDataType {

    private OrderData orderData;
    private List<Leg> strategyLegs;
}
