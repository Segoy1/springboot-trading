package de.segoy.springboottradingibkr.client.service.contract;

import de.segoy.springboottradingdata.model.ContractData;
import de.segoy.springboottradingdata.repository.ContractDataRepository;
import de.segoy.springboottradingdata.service.apiresponsecheck.ApiResponseCheckerForOptional;
import de.segoy.springboottradingibkr.client.service.ApiCaller;
import org.springframework.stereotype.Component;

import java.util.Optional;


@Component
public class ContractDataCallAndResponseHandler {

    private final ContractDataRepository contractDataRepository;
    private final ApiResponseCheckerForOptional<ContractData> contractDataApiResponseChecker;
    private final ApiCaller<ContractData> contractDataApiCaller;

    public ContractDataCallAndResponseHandler(ContractDataRepository contractDataRepository, ApiResponseCheckerForOptional<ContractData> contractDataApiResponseChecker, ApiCaller<ContractData> contractDataApiCaller) {
        this.contractDataRepository = contractDataRepository;
        this.contractDataApiResponseChecker = contractDataApiResponseChecker;
        this.contractDataApiCaller = contractDataApiCaller;
    }

    public Optional<ContractData> callContractDetailsFromAPI(ContractData contractData) {
        //ugly: having to increment by 2 because I am too stupid to do it properly
        int nextId = getNextId(contractData);
        contractData.setId((long) nextId);
        contractDataApiCaller.callApi(contractData);
        return contractDataApiResponseChecker.checkForApiResponseAndUpdate(nextId);
    }


    private int getNextId(ContractData contractData) {
        return contractData.getId() != null ? contractData.getId().intValue() : contractDataRepository.nextValidId() + 1;
    }
}
