package de.segoy.springboottradingibkr.client.service;

import com.ib.client.EClientSocket;
import de.segoy.springboottradingdata.model.ContractData;
import de.segoy.springboottradingdata.model.HistoricalMarketData;
import de.segoy.springboottradingdata.modelconverter.ContractDataToIBKRContract;
import de.segoy.springboottradingdata.repository.HistoricalMarketDataRepository;
import de.segoy.springboottradingibkr.client.datamodel.HistoricalDataSettings;
import de.segoy.springboottradingibkr.client.service.contract.UniqueContractDataProvider;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class HistoricalDataService {

    private final UniqueContractDataProvider uniqueContractDataProvider;
    private final EClientSocket client;
    private final HistoricalMarketDataRepository historicalMarketDataRepository;
    private ContractDataToIBKRContract contractDataToIBKRContract;

    public HistoricalDataService(UniqueContractDataProvider uniqueContractDataProvider, EClientSocket client, HistoricalMarketDataRepository historicalMarketDataRepository, ContractDataToIBKRContract contractDataToIBKRContract) {
        this.uniqueContractDataProvider = uniqueContractDataProvider;
        this.client = client;
        this.historicalMarketDataRepository = historicalMarketDataRepository;
        this.contractDataToIBKRContract = contractDataToIBKRContract;
    }

    public List<HistoricalMarketData> requestHistoricalData(ContractData contractData, HistoricalDataSettings settings) {
        Optional<ContractData> contractDataOptional = uniqueContractDataProvider.getExistingContractDataOrCallApi(contractData);
        contractDataOptional.ifPresent((savedContractData) -> {
           call(savedContractData,settings);
        });
        return new ArrayList<>();
    }

    private void call(ContractData contractData, HistoricalDataSettings settings){
        client.reqHistoricalData(contractData.getContractId(),
                contractDataToIBKRContract.convertContractData(contractData),
                settings.getBackfillEndTime().toString(),
                settings.getBackfillDuration(),
                settings.getBarSizeSetting(),
                settings.getWhatToShow().toString(),
                settings.isRegularTradingHours()?1:0,
                settings.getDateFormatStyle(),
                settings.isKeepUpToDate(),
                settings.getChartOptions());
    }
}
