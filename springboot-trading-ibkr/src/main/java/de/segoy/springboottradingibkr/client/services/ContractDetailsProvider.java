package de.segoy.springboottradingibkr.client.services;

import com.ib.client.Contract;
import de.segoy.springboottradingdata.model.ContractData;
import de.segoy.springboottradingdata.modelconverter.IBKRContractToContractData;
import de.segoy.springboottradingdata.repository.ContractDataRepository;
import org.springframework.stereotype.Component;

@Component
public class ContractDetailsProvider {

    private final IBKRContractToContractData ibkrContractToContractData;
    private final ContractDataRepository contractDataRepository;

    public ContractDetailsProvider(IBKRContractToContractData ibkrContractToContractData, ContractDataRepository contractDataRepository) {
        this.ibkrContractToContractData = ibkrContractToContractData;
        this.contractDataRepository = contractDataRepository;
    }

    public void addContractDetailsFromAPI(int id, Contract contract) {
        ContractData contractData = ibkrContractToContractData.covertIBKRContract(contract);
        contractData.setId(id);

        if(contractDataRepository.findById(id).isPresent()){
            contractDataRepository.save(contractData);
        }else{
            System.out.println("Something went Wrong with Contract Data id: " + id);
        }
    }
}
