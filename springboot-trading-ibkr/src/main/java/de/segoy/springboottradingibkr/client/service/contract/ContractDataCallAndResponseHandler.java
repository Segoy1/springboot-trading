package de.segoy.springboottradingibkr.client.service.contract;

import de.segoy.springboottradingdata.model.data.entity.ContractData;
import de.segoy.springboottradingdata.repository.ContractDataRepository;
import de.segoy.springboottradingibkr.client.service.OptionalApiResponseChecker;
import de.segoy.springboottradingibkr.client.service.ApiCaller;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;


@Component
@RequiredArgsConstructor
public class ContractDataCallAndResponseHandler {

    private final ContractDataRepository contractDataRepository;
    private final OptionalApiResponseChecker<ContractData> contractDataApiResponseChecker;
    private final ApiCaller<ContractData> contractDataApiCaller;


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
