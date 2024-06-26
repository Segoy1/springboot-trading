package de.segoy.springboottradingdata.modelconverter;

import com.ib.client.Bar;
import de.segoy.springboottradingdata.model.data.entity.HistoricalData;
import de.segoy.springboottradingdata.service.IBKRTimeStampFormatter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BarToHistoricalData {

    private final IBKRTimeStampFormatter ibkrTimeStampFormatter;


    public HistoricalData convert(Bar bar){
        return HistoricalData.builder()
                .time(ibkrTimeStampFormatter.formatStringToTimeStamp(bar.time()))
                .open(bar.open())
                .high(bar.high())
                .low(bar.low())
                .close(bar.close())
                .volume(bar.volume().value().stripTrailingZeros())
                .count(bar.count())
                .wap(bar.wap().value().stripTrailingZeros())
                .build();
    }
}
