package de.segoy.springboottradingibkr.client.service.marketdata;

import de.segoy.springboottradingdata.model.data.entity.ContractDbo;
import de.segoy.springboottradingdata.repository.ContractRepository;
import de.segoy.springboottradingibkr.client.service.ApiCaller;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StopAndStartMarketDataService {

    private final ContractRepository contractRepository;
    private final  @Qualifier("StartMarketDataApiCaller") ApiCaller<ContractDbo> startMarketDataApiCaller;
    private final StopMarketDataService stopMarketDataService;

    public void reinitiateApiCall(int id){
        stopMarketDataService.stopMarketDataForTickerId(id);
        contractRepository.findById((long)id).ifPresent(
                startMarketDataApiCaller::callApi);
    }
}
