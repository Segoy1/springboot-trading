package de.segoy.springboottradingibkr.client.service.historicalmarketdata;

import com.ib.client.EClientSocket;
import de.segoy.springboottradingdata.config.PropertiesConfig;
import de.segoy.springboottradingdata.model.ContractData;
import de.segoy.springboottradingdata.model.HistoricalMarketData;
import de.segoy.springboottradingdata.modelconverter.ContractDataToIBKRContract;
import de.segoy.springboottradingdata.service.IBKRTimeStampFormatter;
import de.segoy.springboottradingdata.service.apiresponsecheck.HistoricalMarketDataApiResponseChecker;
import de.segoy.springboottradingibkr.client.datamodel.HistoricalDataSettings;
import de.segoy.springboottradingibkr.client.service.contract.UniqueContractDataProvider;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class HistoricalMarketDataService {

    private final UniqueContractDataProvider uniqueContractDataProvider;
    private final HistoricalResponseListService historicalResponseListService;

    public HistoricalMarketDataService(UniqueContractDataProvider uniqueContractDataProvider, EClientSocket client, HistoricalMarketDataApiResponseChecker historicalMarketDataApiResponseChecker, ContractDataToIBKRContract contractDataToIBKRContract, IBKRTimeStampFormatter ibkrTimeStampFormatter, PropertiesConfig propertiesConfig, HistoricalResponseListService historicalResponseListService) {
        this.uniqueContractDataProvider = uniqueContractDataProvider;
        this.historicalResponseListService = historicalResponseListService;
    }

    public List<HistoricalMarketData> requestHistoricalData(ContractData contractData, HistoricalDataSettings settings) {
        List<HistoricalMarketData> historicalDataTicks = new ArrayList<>();
        Optional<ContractData> contractDataOptional = uniqueContractDataProvider.getExistingContractDataOrCallApi(contractData);
        return contractDataOptional.map((contractData1) -> {
                historicalDataTicks.addAll(historicalResponseListService.getResponseList(settings, contractData1));
                return historicalDataTicks;
        }).orElseGet(() -> historicalDataTicks);
    }


}
