package de.segoy.springboottradingdata.modelconverter;

import com.ib.client.Bar;
import com.ib.client.Decimal;
import de.segoy.springboottradingdata.model.data.entity.HistoricalData;
import de.segoy.springboottradingdata.service.IBKRTimeStampFormatter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BarToHistoricalDataTest {

    @Mock
    private IBKRTimeStampFormatter ibkrTimeStampFormatter;
    @InjectMocks
    private BarToHistoricalData  barToHistoricalData;

    private Bar createSampleBar(){
        return new Bar("20240404", 29.9,35.5, 27.5, 30.5, Decimal.get(10000),57, Decimal.ZERO);
    }

    @Test
    void convertTest(){
        Bar bar = createSampleBar();

        Timestamp time = Timestamp.valueOf(LocalDateTime.now());

        when(ibkrTimeStampFormatter.formatStringToTimeStamp("20240404")).thenReturn(time);

        HistoricalData data = barToHistoricalData.convert(bar);

        assertEquals(time,data.getTime());
        assertEquals(29.9, data.getOpen());
        assertEquals(35.5, data.getHigh());
        assertEquals(30.5, data.getClose());
        assertEquals(27.5, data.getLow());
        assertEquals(57, data.getCount());
        assertEquals(BigDecimal.ZERO, data.getWap());
        assertEquals(0, data.getVolume().compareTo(BigDecimal.valueOf(10000)));
    }

}
