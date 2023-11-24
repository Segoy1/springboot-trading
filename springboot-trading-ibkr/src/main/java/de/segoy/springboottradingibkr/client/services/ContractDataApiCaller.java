package de.segoy.springboottradingibkr.client.services;

import com.ib.client.Contract;
import com.ib.client.EClientSocket;
import com.ib.client.Types;
import de.segoy.springboottradingdata.model.ContractData;
import de.segoy.springboottradingdata.modelconverter.ContractDataToIBKRContract;
import de.segoy.springboottradingdata.repository.ContractDataRepository;
import de.segoy.springboottradingdata.repository.message.ErrorMessageRepository;
import de.segoy.springboottradingdata.service.RepositoryRefreshService;
import org.springframework.stereotype.Component;

@Component
public class ContractDataApiCaller {

    private final EClientSocket client;
    private final ContractDataToIBKRContract contractDataToIBKRContract;
    private final ContractDataRepository contractDataRepository;
    private final RepositoryRefreshService repositoryRefreshService;
    private final ErrorMessageRepository errorMessageRepository;

    public ContractDataApiCaller(EClientSocket client,
                                 ContractDataToIBKRContract contractDataToIBKRContract,
                                 ContractDataRepository contractDataRepository,
                                 RepositoryRefreshService repositoryRefreshService,
                                 ErrorMessageRepository errorMessageRepository) {
        this.client = client;
        this.contractDataToIBKRContract = contractDataToIBKRContract;
        this.contractDataRepository = contractDataRepository;
        this.repositoryRefreshService = repositoryRefreshService;
        this.errorMessageRepository = errorMessageRepository;
    }

    public ContractData callContractDetailsFromAPI(ContractData contractData) {
        Contract ibkrContract = contractDataToIBKRContract.convertContractData(contractData);
        client.reqContractDetails(contractData.getId(), ibkrContract);
        return getUpdatedContractData(contractData.getId());
    }

    private ContractData getUpdatedContractData(Integer id) {
        ContractData savedContactData;
        do {
            repositoryRefreshService.clearCacheAndWait(contractDataRepository);
            savedContactData = contractDataRepository.findById(id).orElseThrow();
        } while (!savedContactData.isTouchedByApi()
                && !savedContactData.getSecurityType().equals(Types.SecType.BAG)
                && !errorMessageRepository.existsById(id));

        return savedContactData;
    }


}
