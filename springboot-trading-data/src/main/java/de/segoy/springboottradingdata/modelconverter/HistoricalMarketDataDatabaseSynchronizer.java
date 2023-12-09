package de.segoy.springboottradingdata.modelconverter;

import com.ib.client.Bar;
import de.segoy.springboottradingdata.model.HistoricalMarketData;
import de.segoy.springboottradingdata.repository.HistoricalMarketDataRepository;
import de.segoy.springboottradingdata.service.IBKRTimeStampFormatter;
import org.springframework.stereotype.Component;

@Component
public class HistoricalMarketDataDatabaseSynchronizer {

    private final HistoricalMarketDataRepository historicalMarketDataRepository;
    private final BarToHistoricalMarketData barToHistoricalMarketData;
    private final IBKRTimeStampFormatter ibkrTimeStampFormatter;

    public HistoricalMarketDataDatabaseSynchronizer(HistoricalMarketDataRepository historicalMarketDataRepository, BarToHistoricalMarketData barToHistoricalMarketData, IBKRTimeStampFormatter ibkrTimeStampFormatter) {
        this.historicalMarketDataRepository = historicalMarketDataRepository;
        this.barToHistoricalMarketData = barToHistoricalMarketData;
        this.ibkrTimeStampFormatter = ibkrTimeStampFormatter;
    }

    public HistoricalMarketData findInDbOrSave(int id, Bar bar) {
        return historicalMarketDataRepository.findFirstByContractIdAndTime(id, ibkrTimeStampFormatter.formatStringToTimeStamp(bar.time()))
                .orElseGet(() -> {
                    HistoricalMarketData newHistoricalMarketData = barToHistoricalMarketData.convert(bar);
                    newHistoricalMarketData.setContractId(id);
                    return historicalMarketDataRepository.save(newHistoricalMarketData);
                });
    }
}
