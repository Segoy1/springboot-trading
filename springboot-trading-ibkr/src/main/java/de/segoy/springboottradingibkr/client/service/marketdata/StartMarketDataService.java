package de.segoy.springboottradingibkr.client.service.marketdata;

import de.segoy.springboottradingdata.model.ContractData;
import de.segoy.springboottradingibkr.client.service.contract.UniqueContractDataProvider;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StartMarketDataService {

    private final StartMarketDataApiCaller startMarketDataApiCaller;
    private final UniqueContractDataProvider uniqueContractDataProvider;


    public StartMarketDataService(StartMarketDataApiCaller startMarketDataApiCaller, UniqueContractDataProvider uniqueContractDataProvider) {
        this.startMarketDataApiCaller = startMarketDataApiCaller;
        this.uniqueContractDataProvider = uniqueContractDataProvider;
    }

    public Optional<ContractData> requestLiveMarketDataForContractData(ContractData contractData) {
        Optional<ContractData> savedContractOptional = uniqueContractDataProvider.getExistingContractDataOrCallApi(contractData);
        savedContractOptional.ifPresent(startMarketDataApiCaller::callApi);
        return savedContractOptional;
    }


}
