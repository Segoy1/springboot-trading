package de.segoy.springboottradingibkr.client.service.historicaldata;

import de.segoy.springboottradingdata.model.data.entity.HistoricalDbo;
import de.segoy.springboottradingdata.model.data.HistoricalDataSettings;
import de.segoy.springboottradingibkr.client.service.contract.UniqueContractDataProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HistoricalDataService {

    private final UniqueContractDataProvider uniqueContractDataProvider;
    private final HistoricalResponseListService historicalResponseListService;


    public List<HistoricalDbo> requestHistoricalData(HistoricalDataSettings settings) {
        return uniqueContractDataProvider.getExistingContractDataOrCallApi(settings.getContractDBO())
                .map((uniqueContractData) -> {
                    settings.setContractDBO(uniqueContractData);
                    return historicalResponseListService.getResponseList(settings);
                }).orElseGet(ArrayList::new);
    }


}
