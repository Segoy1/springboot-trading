package de.segoy.springboottradingibkr.client.service.marketdata;

import de.segoy.springboottradingdata.model.data.entity.ContractData;
import de.segoy.springboottradingibkr.client.service.ApiCaller;
import de.segoy.springboottradingibkr.client.service.contract.UniqueContractDataProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StartMarketDataService {

    private final  @Qualifier("StartMarketDataApiCaller") ApiCaller<ContractData> startMarketDataApiCaller;
    private final UniqueContractDataProvider uniqueContractDataProvider;

    public Optional<ContractData> requestLiveMarketDataForContractData(ContractData contractData) {
        Optional<ContractData> savedContractOptional = uniqueContractDataProvider.getExistingContractDataOrCallApi(contractData);
        savedContractOptional.ifPresent(startMarketDataApiCaller::callApi);
        return savedContractOptional;
    }


}
