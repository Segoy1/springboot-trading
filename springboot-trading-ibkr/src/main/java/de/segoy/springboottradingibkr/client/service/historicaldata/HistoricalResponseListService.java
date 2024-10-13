package de.segoy.springboottradingibkr.client.service.historicaldata;

import de.segoy.springboottradingdata.model.data.entity.HistoricalDbo;
import de.segoy.springboottradingdata.model.data.HistoricalDataSettings;
import de.segoy.springboottradingibkr.client.service.ListApiResponseChecker;
import de.segoy.springboottradingibkr.client.service.ApiCaller;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HistoricalResponseListService {

    private final ListApiResponseChecker<HistoricalDbo> historicalDataApiResponseChecker;
    private final ApiCaller<HistoricalDataSettings> historicalDataApiCaller;


    /**
     * Properties Flag active API Calls set because the Api is called in a Thread that cannot be accessed.
     *
     * @param settings Settings for the Historical Data
     */
    public List<HistoricalDbo> getResponseList(HistoricalDataSettings settings) {

        historicalDataApiCaller.callApi(settings);
        return historicalDataApiResponseChecker.checkForApiResponseAndUpdate(settings.getContractDBO().getContractId());
    }
}
