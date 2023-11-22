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

    public void addContractDetailsFromAPIToContractData(int id, Contract contract) {
        if (contractDataRepository.findAllByContractId(contract.conid()).isEmpty()) {
            ContractData contractData = ibkrContractToContractData.convertIBKRContract(contract);
            contractData.setId(id);
            contractData.setTouchedByApi(true);
            contractDataRepository.save(contractData);
            System.out.println("contractDataId to update: " + id);
        } else {
            System.out.println("Contract with Contract Id: " + contract.conid() + " already exists in DB");
        }
    }
}
