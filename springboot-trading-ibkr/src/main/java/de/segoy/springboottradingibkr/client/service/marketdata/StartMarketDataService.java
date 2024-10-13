package de.segoy.springboottradingibkr.client.service.marketdata;

import de.segoy.springboottradingdata.model.data.entity.ContractDbo;
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

    private final  @Qualifier("StartMarketDataApiCaller") ApiCaller<ContractDbo> startMarketDataApiCaller;
    private final UniqueContractDataProvider uniqueContractDataProvider;

    @Transactional
    public Optional<ContractDbo> requestLiveMarketDataForContractData(ContractDbo contractDBO) {
        Optional<ContractDbo> savedContractOptional = uniqueContractDataProvider.getExistingContractDataOrCallApi(
                contractDBO);
        savedContractOptional.ifPresent(startMarketDataApiCaller::callApi);
        return savedContractOptional;
    }


}
