package de.segoy.springboottradingibkr.client.services;

import com.ib.client.Contract;
import com.ib.client.EClientSocket;
import de.segoy.springboottradingdata.model.ContractData;
import de.segoy.springboottradingdata.modelconverter.ContractDataToIBKRContract;
import de.segoy.springboottradingdata.repository.ContractDataRepository;
import org.springframework.stereotype.Component;

@Component
public class ContractDataApiCaller {

    private final EClientSocket client;
    private final ContractDataToIBKRContract contractDataToIBKRContract;
    private final ContractDataRepository contractDataRepository;

    public ContractDataApiCaller(EClientSocket client, ContractDataToIBKRContract contractDataToIBKRContract, ContractDataRepository contractDataRepository) {
        this.client = client;
        this.contractDataToIBKRContract = contractDataToIBKRContract;
        this.contractDataRepository = contractDataRepository;
    }

    public ContractData callContractDetailsFromAPI(ContractData contractData){
        Contract ibkrContract = contractDataToIBKRContract.convertContractData(contractData);
        client.reqContractDetails(contractData.getId(), ibkrContract);
        return contractDataRepository.findById(contractData.getId()).orElse(contractData);
    }
}
