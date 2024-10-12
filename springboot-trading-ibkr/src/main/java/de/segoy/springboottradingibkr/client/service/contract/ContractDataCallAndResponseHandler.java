package de.segoy.springboottradingibkr.client.service.contract;

import de.segoy.springboottradingdata.model.data.entity.ContractDataDBO;
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
    private final OptionalApiResponseChecker<ContractDataDBO> contractDataApiResponseChecker;
    private final ApiCaller<ContractDataDBO> contractDataApiCaller;


    public Optional<ContractDataDBO> callContractDetailsFromAPI(ContractDataDBO contractDataDBO) {
        //ugly: having to increment by 2 because I am too stupid to do it properly
        int nextId = getNextId(contractDataDBO);
        contractDataDBO.setId((long) nextId);
        contractDataApiCaller.callApi(contractDataDBO);
        return contractDataApiResponseChecker.checkForApiResponseAndUpdate(nextId);
    }


    private int getNextId(ContractDataDBO contractDataDBO) {
        return contractDataDBO.getId() != null ? contractDataDBO.getId().intValue() : contractDataRepository.nextValidId() + 1;
    }
}
