package de.segoy.springboottradingdata.modelconverter;

import com.ib.client.Contract;
import de.segoy.springboottradingdata.model.PositionData;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class IBKRResponseToPositionData {

    private IBKRContractToContractData ibkrContractToContractData;

    public IBKRResponseToPositionData(IBKRContractToContractData ibkrContractToContractData) {
        this.ibkrContractToContractData = ibkrContractToContractData;
    }

    public PositionData convert(String account, Contract contract, BigDecimal position, double avgCost){
        return PositionData.builder().account(account)
                .contractData(ibkrContractToContractData.convertIBKRContract(contract))
                .position(position)
                .averageCost(avgCost).build();
    }
}
