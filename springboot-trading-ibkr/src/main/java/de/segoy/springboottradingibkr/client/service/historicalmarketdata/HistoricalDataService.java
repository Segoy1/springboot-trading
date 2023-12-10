package de.segoy.springboottradingibkr.client.service.historicalmarketdata;

import com.ib.client.EClientSocket;
import de.segoy.springboottradingdata.config.PropertiesConfig;
import de.segoy.springboottradingdata.model.HistoricalData;
import de.segoy.springboottradingdata.modelconverter.ContractDataToIBKRContract;
import de.segoy.springboottradingdata.service.IBKRTimeStampFormatter;
import de.segoy.springboottradingdata.service.apiresponsecheck.HistoricalMarketDataApiResponseChecker;
import de.segoy.springboottradingibkr.client.datamodel.HistoricalDataSettings;
import de.segoy.springboottradingibkr.client.service.contract.UniqueContractDataProvider;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HistoricalDataService {

    private final UniqueContractDataProvider uniqueContractDataProvider;
    private final HistoricalResponseListService historicalResponseListService;

    public HistoricalDataService(UniqueContractDataProvider uniqueContractDataProvider, EClientSocket client, HistoricalMarketDataApiResponseChecker historicalMarketDataApiResponseChecker, ContractDataToIBKRContract contractDataToIBKRContract, IBKRTimeStampFormatter ibkrTimeStampFormatter, PropertiesConfig propertiesConfig, HistoricalResponseListService historicalResponseListService) {
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
