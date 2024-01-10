package de.segoy.springboottradingdata.model;

import de.segoy.springboottradingdata.model.entity.IBKRDataTypeEntity;
import de.segoy.springboottradingdata.model.entity.OrderData;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StrategyData extends IBKRDataTypeEntity {

    private OrderData orderData;
    private List<Leg> strategyLegs;
}
