package de.segoy.springboottradingdata.modelsynchronize;

import com.ib.client.Bar;
import de.segoy.springboottradingdata.model.data.entity.HistoricalDataDBO;
import de.segoy.springboottradingdata.modelconverter.BarToHistoricalData;
import de.segoy.springboottradingdata.repository.HistoricalDataRepository;
import de.segoy.springboottradingdata.service.IBKRTimeStampFormatter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HistoricalDataDatabaseSynchronizer {

    private final HistoricalDataRepository historicalDataRepository;
    private final BarToHistoricalData barToHistoricalData;
    private final IBKRTimeStampFormatter ibkrTimeStampFormatter;

    public HistoricalDataDBO findInDbOrSave(int id, Bar bar) {
        return historicalDataRepository.findFirstByContractIdAndTimeAndCount(id, ibkrTimeStampFormatter.formatStringToTimeStamp(bar.time()), bar.count())
                .orElseGet(() -> {
                    HistoricalDataDBO newHistoricalDataDBO = barToHistoricalData.convert(bar);
                    newHistoricalDataDBO.setContractId(id);
                    return historicalDataRepository.save(newHistoricalDataDBO);
                });
    }
}
