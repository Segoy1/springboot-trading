package de.segoy.springboottradingdata.modelconverter;

import com.ib.client.Contract;
import de.segoy.springboottradingdata.model.data.entity.ContractDataDBO;
import de.segoy.springboottradingdata.model.data.entity.PositionDataDBO;
import de.segoy.springboottradingdata.modelsynchronize.ContractDataDatabaseSynchronizer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.OptionalLong;

@Component
@RequiredArgsConstructor
public class IBKRResponseToPositionData {

    private final ContractDataDatabaseSynchronizer contractDataDatabaseSynchronizer;

    public PositionDataDBO convertAndPersistContract(String account, Contract contract, BigDecimal position,
                                                     double avgCost){
        ContractDataDBO contractDataDBO =
                contractDataDatabaseSynchronizer.findInDBOrConvertAndSaveOrUpdateIfIdIsProvided(OptionalLong.empty(),
                contract);
        return PositionDataDBO.builder().account(account)
                .position(position)
                .contractDataDBO(contractDataDBO)
                .averageCost(avgCost)
                .totalCost(avgCost * position.doubleValue()).build();
    }
}
