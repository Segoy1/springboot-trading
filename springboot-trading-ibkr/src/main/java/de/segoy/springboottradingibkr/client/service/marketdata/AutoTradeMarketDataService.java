package de.segoy.springboottradingibkr.client.service.marketdata;

import de.segoy.springboottradingdata.model.data.entity.ContractDbo;
import de.segoy.springboottradingibkr.client.service.contract.UniqueContractDataProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class AutoTradeMarketDataService {

    private final AutoTradeStartMarketDataApiCaller autoTradeStartMarketDataApiCaller;
    private final UniqueContractDataProvider uniqueContractDataProvider;

    @Transactional
    public Optional<ContractDbo> requestLiveMarketDataForContractData(int id, ContractDbo contractDBO) {
        Optional<ContractDbo> savedContractOptional = uniqueContractDataProvider.getExistingContractDataOrCallApi(
                contractDBO);
        savedContractOptional.ifPresent(savedContract ->
                autoTradeStartMarketDataApiCaller.callApiWithId(id,savedContract));
        return savedContractOptional;
    }
}
