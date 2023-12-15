package de.segoy.springboottradingdata.modelsynchronize;

import com.ib.client.Bar;
import de.segoy.springboottradingdata.model.HistoricalData;
import de.segoy.springboottradingdata.modelconverter.BarToHistoricalData;
import de.segoy.springboottradingdata.repository.HistoricalDataRepository;
import de.segoy.springboottradingdata.service.IBKRTimeStampFormatter;
import org.springframework.stereotype.Component;

@Component
public class HistoricalDataDatabaseSynchronizer {

    private final HistoricalDataRepository historicalDataRepository;
    private final BarToHistoricalData barToHistoricalData;
    private final IBKRTimeStampFormatter ibkrTimeStampFormatter;

    public HistoricalDataDatabaseSynchronizer(HistoricalDataRepository historicalDataRepository, BarToHistoricalData barToHistoricalData, IBKRTimeStampFormatter ibkrTimeStampFormatter) {
        this.historicalDataRepository = historicalDataRepository;
        this.barToHistoricalData = barToHistoricalData;
        this.ibkrTimeStampFormatter = ibkrTimeStampFormatter;
    }

    public HistoricalData findInDbOrSave(int id, Bar bar) {
        return historicalDataRepository.findFirstByContractIdAndTimeAndCount(id, ibkrTimeStampFormatter.formatStringToTimeStamp(bar.time()), bar.count())
                .orElseGet(() -> {
                    HistoricalData newHistoricalData = barToHistoricalData.convert(bar);
                    newHistoricalData.setContractId(id);
                    return historicalDataRepository.save(newHistoricalData);
                });
    }
}
