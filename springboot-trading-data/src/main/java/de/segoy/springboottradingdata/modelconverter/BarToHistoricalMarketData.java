package de.segoy.springboottradingdata.modelconverter;

import com.ib.client.Bar;
import de.segoy.springboottradingdata.model.HistoricalMarketData;
import de.segoy.springboottradingdata.service.IBKRTimeStampFormatter;
import org.springframework.stereotype.Component;

@Component
public class BarToHistoricalMarketData {

    private final IBKRTimeStampFormatter ibkrTimeStampFormatter;

    public BarToHistoricalMarketData(IBKRTimeStampFormatter ibkrTimeStampFormatter) {
        this.ibkrTimeStampFormatter = ibkrTimeStampFormatter;
    }

    public HistoricalMarketData convert(Bar bar){
        return HistoricalMarketData.builder()
                .time(ibkrTimeStampFormatter.formatStringToTimeStamp(bar.time()))
                .open(bar.open())
                .high(bar.high())
                .low(bar.low())
                .close(bar.close())
                .volume(bar.volume().value())
                .count(bar.count())
                .wap(bar.wap().value())
                .build();
    }
}
