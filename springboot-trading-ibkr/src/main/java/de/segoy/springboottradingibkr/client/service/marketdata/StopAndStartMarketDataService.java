package de.segoy.springboottradingibkr.client.service.marketdata;

import de.segoy.springboottradingdata.model.data.entity.ContractDataDBO;
import de.segoy.springboottradingdata.repository.ContractDataRepository;
import de.segoy.springboottradingibkr.client.service.ApiCaller;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StopAndStartMarketDataService {

    private final ContractDataRepository contractDataRepository;
    private final  @Qualifier("StartMarketDataApiCaller") ApiCaller<ContractDataDBO> startMarketDataApiCaller;
    private final StopMarketDataService stopMarketDataService;

    public void reinitiateApiCall(int id){
        stopMarketDataService.stopMarketDataForTickerId(id);
        contractDataRepository.findById((long)id).ifPresent(
                startMarketDataApiCaller::callApi);
    }
}
