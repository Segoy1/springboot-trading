package de.segoy.springboottradingdata.modelconverter;

import com.ib.client.Contract;
import de.segoy.springboottradingdata.model.data.entity.ContractDbo;
import de.segoy.springboottradingdata.model.data.entity.PositionDbo;
import de.segoy.springboottradingdata.modelsynchronize.ContractDataDatabaseSynchronizer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.OptionalLong;

@Component
@RequiredArgsConstructor
public class IBKRToPositionDbo {

    private final ContractDataDatabaseSynchronizer contractDataDatabaseSynchronizer;

    public PositionDbo convertAndPersistContract(String account, Contract contract, BigDecimal position,
                                                 double avgCost){
        ContractDbo contractDBO =
                contractDataDatabaseSynchronizer.findInDBOrConvertAndSaveOrUpdateIfIdIsProvided(OptionalLong.empty(),
                contract);
        BigDecimal averageCost = BigDecimal.valueOf(avgCost * position.signum());
        return PositionDbo.builder().account(account)
                .position(position)
                .contractDBO(contractDBO)
                .averageCost(averageCost)
                .totalCost(averageCost.multiply(position)).build();
    }
}
