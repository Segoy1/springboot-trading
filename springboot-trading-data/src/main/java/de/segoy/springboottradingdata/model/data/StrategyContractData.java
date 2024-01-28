package de.segoy.springboottradingdata.model.data;

import de.segoy.springboottradingdata.model.Leg;
import de.segoy.springboottradingdata.model.data.entity.ContractData;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StrategyContractData extends IBKRDataType {

    private ContractData contractData;
    private List<Leg> strategyLegs;
}
