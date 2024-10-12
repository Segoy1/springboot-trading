package de.segoy.springboottradingibkr.client.service.marketdata;

import de.segoy.springboottradingdata.model.data.entity.ContractDataDBO;
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
    public Optional<ContractDataDBO> requestLiveMarketDataForContractData(int id, ContractDataDBO contractDataDBO) {
        Optional<ContractDataDBO> savedContractOptional = uniqueContractDataProvider.getExistingContractDataOrCallApi(
                contractDataDBO);
        savedContractOptional.ifPresent(savedContract ->
                autoTradeStartMarketDataApiCaller.callApiWithId(id,savedContract));
        return savedContractOptional;
    }
}
