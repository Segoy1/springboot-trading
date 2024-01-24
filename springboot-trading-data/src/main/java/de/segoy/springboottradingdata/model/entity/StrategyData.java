package de.segoy.springboottradingdata.model.entity;

import de.segoy.springboottradingdata.model.Leg;
import de.segoy.springboottradingdata.model.entity.IBKRDataTypeEntity;
import de.segoy.springboottradingdata.model.entity.database.OrderData;
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
