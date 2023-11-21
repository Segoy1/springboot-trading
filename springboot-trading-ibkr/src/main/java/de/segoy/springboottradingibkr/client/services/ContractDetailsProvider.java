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

    public synchronized void addContractDetailsFromAPIToContractData(int id, Contract contract) {
        ContractData contractData = ibkrContractToContractData.covertIBKRContract(contract);
        contractData.setId(id);

        contractDataRepository.save(contractData);
        System.out.println("contractDataId to update: " + id);
    }
}
