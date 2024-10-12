package de.segoy.springboottradingibkr.client.service.marketdata;

import de.segoy.springboottradingdata.model.data.entity.ContractDataDBO;
import de.segoy.springboottradingibkr.client.service.ApiCaller;
import de.segoy.springboottradingibkr.client.service.contract.UniqueContractDataProvider;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StartMarketDataService {

    private final  @Qualifier("StartMarketDataApiCaller") ApiCaller<ContractDataDBO> startMarketDataApiCaller;
    private final UniqueContractDataProvider uniqueContractDataProvider;

    @Transactional
    public Optional<ContractDataDBO> requestLiveMarketDataForContractData(ContractDataDBO contractDataDBO) {
        Optional<ContractDataDBO> savedContractOptional = uniqueContractDataProvider.getExistingContractDataOrCallApi(
                contractDataDBO);
        savedContractOptional.ifPresent(startMarketDataApiCaller::callApi);
        return savedContractOptional;
    }


}
