package de.segoy.springboottradingdata.modelconverter;

import com.ib.client.Bar;
import de.segoy.springboottradingdata.model.HistoricalData;
import de.segoy.springboottradingdata.repository.HistoricalDataRepository;
import de.segoy.springboottradingdata.service.IBKRTimeStampFormatter;
import org.springframework.stereotype.Component;

@Component
public class HistoricalMarketDataDatabaseSynchronizer {

    private final HistoricalDataRepository historicalDataRepository;
    private final BarToHistoricalMarketData barToHistoricalMarketData;
    private final IBKRTimeStampFormatter ibkrTimeStampFormatter;

    public HistoricalMarketDataDatabaseSynchronizer(HistoricalDataRepository historicalDataRepository, BarToHistoricalMarketData barToHistoricalMarketData, IBKRTimeStampFormatter ibkrTimeStampFormatter) {
        this.historicalDataRepository = historicalDataRepository;
        this.barToHistoricalMarketData = barToHistoricalMarketData;
        this.ibkrTimeStampFormatter = ibkrTimeStampFormatter;
    }

    public HistoricalData findInDbOrSave(int id, Bar bar) {
        return historicalDataRepository.findFirstByContractIdAndTime(id, ibkrTimeStampFormatter.formatStringToTimeStamp(bar.time()))
                .orElseGet(() -> {
                    HistoricalData newHistoricalData = barToHistoricalMarketData.convert(bar);
                    newHistoricalData.setContractId(id);
                    return historicalDataRepository.save(newHistoricalData);
                });
    }
}
