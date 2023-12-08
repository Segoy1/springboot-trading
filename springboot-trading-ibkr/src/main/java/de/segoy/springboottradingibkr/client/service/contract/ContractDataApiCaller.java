package de.segoy.springboottradingibkr.client.service.contract;

import com.ib.client.Contract;
import com.ib.client.EClientSocket;
import de.segoy.springboottradingdata.model.ContractData;
import de.segoy.springboottradingdata.modelconverter.ContractDataToIBKRContract;
import de.segoy.springboottradingdata.repository.ContractDataRepository;
import de.segoy.springboottradingdata.service.apiresponsecheck.ContractDataApiResponseChecker;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ContractDataApiCaller {

    private final EClientSocket client;
    private final ContractDataToIBKRContract contractDataToIBKRContract;
    private final ContractDataRepository contractDataRepository;
    private final ContractDataApiResponseChecker contractDataApiResponseChecker;

    public ContractDataApiCaller(EClientSocket client,
                                 ContractDataToIBKRContract contractDataToIBKRContract,
                                 ContractDataRepository contractDataRepository,
                                ContractDataApiResponseChecker contractDataApiResponseChecker) {
        this.client = client;
        this.contractDataToIBKRContract = contractDataToIBKRContract;
        this.contractDataRepository = contractDataRepository;
        this.contractDataApiResponseChecker = contractDataApiResponseChecker;
    }

    public Optional<ContractData> callContractDetailsFromAPI(ContractData contractData) {
        Contract ibkrContract = contractDataToIBKRContract.convertContractData(contractData);
        //ugly: having to increment by 2 because I am too stupid to do it properly
        Long nextId = getNextId(contractData);
        client.reqContractDetails(nextId.intValue(), ibkrContract);
        return contractDataApiResponseChecker.checkForApiResponseAndUpdate(nextId);
    }

    private Long getNextId(ContractData contractData) {
        return contractData.getId() != null ? contractData.getId() : (long) contractDataRepository.nextValidId() + 1;
    }
}
