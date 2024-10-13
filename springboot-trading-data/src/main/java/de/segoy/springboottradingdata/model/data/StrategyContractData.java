package de.segoy.springboottradingdata.model.data;

import de.segoy.springboottradingdata.model.Leg;
import de.segoy.springboottradingdata.model.data.entity.ContractDbo;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StrategyContractData extends IBKRDataType {

    private ContractDbo contractDBO;
    private List<Leg> strategyLegs;
}
