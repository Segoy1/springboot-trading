package de.segoy.springboottradingibkr.client.service;

import com.ib.client.EClientSocket;
import de.segoy.springboottradingdata.model.ContractData;
import de.segoy.springboottradingdata.model.IBKRDataTypeEntity;
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
    private final EClientSocket client;
    private final HistoricalMarketDataApiResponseChecker historicalMarketDataApiResponseChecker;
    private final ContractDataToIBKRContract contractDataToIBKRContract;
    private final IBKRTimeStampFormatter ibkrTimeStampFormatter;

    public HistoricalMarketDataService(UniqueContractDataProvider uniqueContractDataProvider, EClientSocket client, HistoricalMarketDataApiResponseChecker historicalMarketDataApiResponseChecker, ContractDataToIBKRContract contractDataToIBKRContract, IBKRTimeStampFormatter ibkrTimeStampFormatter) {
        this.uniqueContractDataProvider = uniqueContractDataProvider;
        this.client = client;
        this.historicalMarketDataApiResponseChecker = historicalMarketDataApiResponseChecker;
        this.contractDataToIBKRContract = contractDataToIBKRContract;
        this.ibkrTimeStampFormatter = ibkrTimeStampFormatter;
    }

    public List<IBKRDataTypeEntity> requestHistoricalData(ContractData contractData, HistoricalDataSettings settings) {
        List<IBKRDataTypeEntity> ticks = new ArrayList<>();
        Optional<ContractData> contractDataOptional = uniqueContractDataProvider.getExistingContractDataOrCallApi(contractData);
        contractDataOptional.ifPresent((savedContractData) -> {
            requestDataFromApi(savedContractData, settings);
            ticks.addAll(historicalMarketDataApiResponseChecker.checkForApiResponseAndUpdate(savedContractData.getContractId()));
        });

        return ticks;
    }

    private void requestDataFromApi(ContractData contractData, HistoricalDataSettings settings) {
        client.reqHistoricalData(contractData.getContractId(),
                contractDataToIBKRContract.convertContractData(contractData),
                ibkrTimeStampFormatter.formatTimestampToDateAndTime(settings.getBackfillEndTime()),
                settings.getBackfillDuration(),
                settings.getBarSizeSetting(),
                settings.getWhatToShow().toString(),
                settings.isRegularTradingHours() ? 1 : 0,
                settings.getDateFormatStyle(),
                settings.isKeepUpToDate(),
                settings.getChartOptions());
    }
}
