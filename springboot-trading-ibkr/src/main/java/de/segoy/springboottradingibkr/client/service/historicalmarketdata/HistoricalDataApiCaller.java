package de.segoy.springboottradingibkr.client.service.historicalmarketdata;

import com.ib.client.EClientSocket;
import de.segoy.springboottradingdata.config.PropertiesConfig;
import de.segoy.springboottradingdata.model.ContractData;
import de.segoy.springboottradingdata.modelconverter.ContractDataToIBKRContract;
import de.segoy.springboottradingdata.service.IBKRTimeStampFormatter;
import de.segoy.springboottradingibkr.client.datamodel.HistoricalDataSettings;
import org.springframework.stereotype.Service;

@Service
public class HistoricalDataApiCaller {

    private final EClientSocket client;
    private final ContractDataToIBKRContract contractDataToIBKRContract;
    private final IBKRTimeStampFormatter ibkrTimeStampFormatter;
    private final PropertiesConfig propertiesConfig;

    public HistoricalDataApiCaller(EClientSocket client, ContractDataToIBKRContract contractDataToIBKRContract, IBKRTimeStampFormatter ibkrTimeStampFormatter, PropertiesConfig propertiesConfig) {
        this.client = client;
        this.contractDataToIBKRContract = contractDataToIBKRContract;
        this.ibkrTimeStampFormatter = ibkrTimeStampFormatter;
        this.propertiesConfig = propertiesConfig;
    }

    public void callApi(ContractData contractData, HistoricalDataSettings settings) {
        propertiesConfig.addToActiveApiCalls((long) contractData.getContractId());
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
