package de.segoy.springboottradingibkr.client.services;

import com.ib.client.Contract;
import com.ib.client.EClientSocket;
import de.segoy.springboottradingdata.model.ContractData;
import de.segoy.springboottradingdata.modelconverter.ContractDataToIBKRContract;
import org.springframework.stereotype.Component;

@Component
public class ContractDataDetailsProvider {

    private final EClientSocket client;
    private final ContractDataToIBKRContract contractDataToIBKRContract;

    public ContractDataDetailsProvider(EClientSocket client, ContractDataToIBKRContract contractDataToIBKRContract) {
        this.client = client;
        this.contractDataToIBKRContract = contractDataToIBKRContract;
    }

    public ContractData requestContractDetails(ContractData contractData){
        Contract ibkrContract = contractDataToIBKRContract.convertContractData(contractData);

        client.reqContractDetails(contractData.getId(), ibkrContract);


        return contractData;
    }
}
