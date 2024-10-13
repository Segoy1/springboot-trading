package de.segoy.springboottradingibkr.client.service.historicaldata;

import com.ib.client.EClientSocket;
import de.segoy.springboottradingdata.modelconverter.ContractDboToIBKRContract;
import de.segoy.springboottradingdata.service.IBKRTimeStampFormatter;
import de.segoy.springboottradingdata.model.data.HistoricalDataSettings;
import de.segoy.springboottradingibkr.client.service.ApiCaller;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class HistoricalDataApiCaller implements ApiCaller<HistoricalDataSettings> {

    private final EClientSocket client;
    private final ContractDboToIBKRContract contractDboToIBKRContract;
    private final IBKRTimeStampFormatter ibkrTimeStampFormatter;

    public void callApi(HistoricalDataSettings settings) {
        client.reqHistoricalData(settings.getContractDBO().getContractId(),
                contractDboToIBKRContract.convertContractData(settings.getContractDBO()),
                ibkrTimeStampFormatter.formatTimestampToDateAndTime(settings.getBackfillEndTime()),
                settings.getBackfillDuration(),
                settings.getBarSizeSetting().getValue(),
                settings.getWhatToShow().toString(),
                settings.isRegularTradingHours() ? 1 : 0,
                settings.getDateFormatStyle(),
                settings.isKeepUpToDate(),
                settings.getChartOptions());
    }
}
