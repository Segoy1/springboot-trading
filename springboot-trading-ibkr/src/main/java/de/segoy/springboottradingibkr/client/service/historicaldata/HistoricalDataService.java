package de.segoy.springboottradingibkr.client.service.historicaldata;

import de.segoy.springboottradingdata.model.HistoricalData;
import de.segoy.springboottradingdata.model.HistoricalDataSettings;
import de.segoy.springboottradingibkr.client.service.contract.UniqueContractDataProvider;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HistoricalDataService {

    private final UniqueContractDataProvider uniqueContractDataProvider;
    private final HistoricalResponseListService historicalResponseListService;

    public HistoricalDataService(UniqueContractDataProvider uniqueContractDataProvider, HistoricalResponseListService historicalResponseListService) {
        this.uniqueContractDataProvider = uniqueContractDataProvider;
        this.historicalResponseListService = historicalResponseListService;
    }

    public List<HistoricalData> requestHistoricalData(HistoricalDataSettings settings) {
        return uniqueContractDataProvider.getExistingContractDataOrCallApi(settings.getContractData())
                .map((uniqueContractData) -> {
                    settings.setContractData(uniqueContractData);
                    return historicalResponseListService.getResponseList(settings);
                }).orElseGet(ArrayList::new);
    }


}
