package de.segoy.springboottradingdata.modelconverter;

import com.ib.client.Contract;
import de.segoy.springboottradingdata.model.data.entity.ContractData;
import de.segoy.springboottradingdata.model.data.entity.PositionData;
import de.segoy.springboottradingdata.modelsynchronize.ContractDataDatabaseSynchronizer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.OptionalLong;

@Component
@RequiredArgsConstructor
public class IBKRResponseToPositionData {

    private final ContractDataDatabaseSynchronizer contractDataDatabaseSynchronizer;

    public PositionData convertAndPersistContract(String account, Contract contract, BigDecimal position,
                                                 double avgCost){
        ContractData contractData =
                contractDataDatabaseSynchronizer.findInDBOrConvertAndSaveOrUpdateIfIdIsProvided(OptionalLong.empty(),
                contract);
        return PositionData.builder().account(account)
                .position(position)
                .contractData(contractData)
                .averageCost(avgCost)
                .totalCost(avgCost * position.doubleValue()).build();
    }
}
