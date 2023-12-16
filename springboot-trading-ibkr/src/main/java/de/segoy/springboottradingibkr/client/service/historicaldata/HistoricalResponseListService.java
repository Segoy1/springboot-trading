package de.segoy.springboottradingibkr.client.service.historicaldata;

import de.segoy.springboottradingdata.model.entity.HistoricalData;
import de.segoy.springboottradingdata.model.HistoricalDataSettings;
import de.segoy.springboottradingdata.service.apiresponsecheck.ListApiResponseChecker;
import de.segoy.springboottradingibkr.client.service.ApiCaller;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HistoricalResponseListService {

    private final ListApiResponseChecker<HistoricalData> historicalDataApiResponseChecker;
    private final ApiCaller<HistoricalDataSettings> historicalDataApiCaller;

    public HistoricalResponseListService(ListApiResponseChecker<HistoricalData> historicalDataApiResponseChecker, ApiCaller<HistoricalDataSettings> historicalDataApiCaller) {
        this.historicalDataApiResponseChecker = historicalDataApiResponseChecker;
        this.historicalDataApiCaller = historicalDataApiCaller;
    }

    /**
     * Properties Flag active API Calls set because the Api is called in a Thread that cannot be accessed.
     *
     * @param settings Settings for the Historical Data
     */
    public List<HistoricalData> getResponseList(HistoricalDataSettings settings) {

        historicalDataApiCaller.callApi(settings);
        return historicalDataApiResponseChecker.checkForApiResponseAndUpdate(settings.getContractData().getContractId());
    }
}
