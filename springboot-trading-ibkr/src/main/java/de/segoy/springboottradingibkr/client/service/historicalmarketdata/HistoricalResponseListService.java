package de.segoy.springboottradingibkr.client.service.historicalmarketdata;

import com.ib.client.EClientSocket;
import de.segoy.springboottradingdata.config.PropertiesConfig;
import de.segoy.springboottradingdata.model.ContractData;
import de.segoy.springboottradingdata.model.IBKRDataTypeEntity;
import de.segoy.springboottradingdata.modelconverter.ContractDataToIBKRContract;
import de.segoy.springboottradingdata.service.IBKRTimeStampFormatter;
import de.segoy.springboottradingdata.service.apiresponsecheck.HistoricalMarketDataApiResponseChecker;
import de.segoy.springboottradingibkr.client.datamodel.HistoricalDataSettings;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HistoricalResponseListService {

    private final PropertiesConfig propertiesConfig;
    private final HistoricalMarketDataApiResponseChecker historicalMarketDataApiResponseChecker;
    private final EClientSocket client;
    private final ContractDataToIBKRContract contractDataToIBKRContract;
    private final IBKRTimeStampFormatter ibkrTimeStampFormatter;
    private final HistoricalDataApiCaller historicalDataApiCaller;

    public HistoricalResponseListService(PropertiesConfig propertiesConfig, HistoricalMarketDataApiResponseChecker historicalMarketDataApiResponseChecker, EClientSocket client, ContractDataToIBKRContract contractDataToIBKRContract, IBKRTimeStampFormatter ibkrTimeStampFormatter, HistoricalDataApiCaller historicalDataApiCaller) {
        this.propertiesConfig = propertiesConfig;
        this.historicalMarketDataApiResponseChecker = historicalMarketDataApiResponseChecker;
        this.client = client;
        this.contractDataToIBKRContract = contractDataToIBKRContract;
        this.ibkrTimeStampFormatter = ibkrTimeStampFormatter;
        this.historicalDataApiCaller = historicalDataApiCaller;
    }

    /**
     * Properties Flag active API Calls set because the Api is called in a Thread that cannot be accessed.
     *
     * @param settings Settings for the Historical Data
     * @param contractData Contract that historical Data will be called off of
     */
    public List<IBKRDataTypeEntity> getResponseList(HistoricalDataSettings settings, ContractData contractData) {
        propertiesConfig.addToActiveApiCalls((long) contractData.getContractId());
        historicalDataApiCaller.callApi(contractData,settings);
        return (historicalMarketDataApiResponseChecker.checkForApiResponseAndUpdate(contractData.getContractId()));
    }
}
