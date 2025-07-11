package de.segoy.springboottradingibkr.client.service.contract;

import de.segoy.springboottradingdata.model.data.entity.ContractDbo;
import de.segoy.springboottradingdata.repository.ContractRepository;
import de.segoy.springboottradingibkr.client.service.ApiCaller;
import de.segoy.springboottradingibkr.client.service.OptionalApiResponseChecker;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ContractDataCallAndResponseHandler {

    private final ContractRepository contractRepository;
    private final OptionalApiResponseChecker<ContractDbo> contractDataApiResponseChecker;
    private final ApiCaller<ContractDbo> contractDataApiCaller;


    public Optional<ContractDbo> callContractDetailsFromAPI(ContractDbo contractDBO) {
        //ugly: having to increment by 2 because I am too stupid to do it properly
        int nextId = getNextId(contractDBO);
        contractDBO.setId((long) nextId);
        contractDataApiCaller.callApi(contractDBO);
        return contractDataApiResponseChecker.checkForApiResponseAndUpdate(nextId);
    }


    private int getNextId(ContractDbo contractDBO) {
        return contractDBO.getId() != null ? contractDBO.getId().intValue() : contractRepository.nextValidId() + 1;
    }
}
