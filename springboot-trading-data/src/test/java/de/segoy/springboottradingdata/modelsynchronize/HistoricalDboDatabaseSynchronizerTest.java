package de.segoy.springboottradingdata.modelsynchronize;

import com.ib.client.Bar;
import com.ib.client.Decimal;
import de.segoy.springboottradingdata.model.data.entity.HistoricalDbo;
import de.segoy.springboottradingdata.modelconverter.BarToHistoricalDataDbo;
import de.segoy.springboottradingdata.repository.HistoricalRepository;
import de.segoy.springboottradingdata.service.IBKRTimeStampFormatter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HistoricalDboDatabaseSynchronizerTest {

    @Mock
    private HistoricalRepository historicalRepository;
    @Mock
    private BarToHistoricalDataDbo barToHistoricalDataDbo;
    @Mock
    private IBKRTimeStampFormatter ibkrTimeStampFormatter;
    @InjectMocks
    private HistoricalDataDatabaseSynchronizer historicalDataDatabaseSynchronizer;

    @Test
    void testFindInDb(){

        Bar bar = new Bar("20240404",1.0,2.0,0.0,1.0, Decimal.ONE_HUNDRED,50,Decimal.ZERO);
        Timestamp time = Timestamp.valueOf(LocalDateTime.now());
        HistoricalDbo result =
                HistoricalDbo.builder().id(1L).close(1.0).contractId(1).low(0.0).open(1.0).high(2.0).volume(BigDecimal.valueOf(100)).count(50).wap(BigDecimal.ZERO).build();

        when(ibkrTimeStampFormatter.formatStringToTimeStamp(bar.time())).thenReturn(time);
        when(historicalRepository.findFirstByContractIdAndTimeAndCount(1, time, bar.count())).thenReturn(Optional.of(result));

        historicalDataDatabaseSynchronizer.findInDbOrSave(1,bar);

        assertEquals(1, result.getContractId());
        assertEquals(1, result.getId());
        assertEquals(0.0, result.getLow());
        assertEquals(1.0, result.getOpen());
        assertEquals(1.0, result.getClose());
        assertEquals(2.0, result.getHigh());
        assertEquals(BigDecimal.valueOf(100), result.getVolume());

        verify(ibkrTimeStampFormatter, times(1)).formatStringToTimeStamp(bar.time());
        verify(historicalRepository, times(1)).findFirstByContractIdAndTimeAndCount(1,time, bar.count());

    }

    @Test
    void testSaveToDb(){

        Bar bar = new Bar("20240404",1.0,2.0,0.0,1.0, Decimal.ONE_HUNDRED,50,Decimal.ZERO);
        Timestamp time = Timestamp.valueOf(LocalDateTime.now());
        HistoricalDbo result =
                HistoricalDbo.builder().close(1.0).low(0.0).open(1.0).high(2.0).volume(BigDecimal.valueOf(100)).count(50).wap(BigDecimal.ZERO).build();

        when(ibkrTimeStampFormatter.formatStringToTimeStamp(bar.time())).thenReturn(time);
        when(historicalRepository.findFirstByContractIdAndTimeAndCount(1, time, bar.count())).thenReturn(Optional.empty());
        when(barToHistoricalDataDbo.convert(bar)).thenReturn(result);

        historicalDataDatabaseSynchronizer.findInDbOrSave(1,bar);

        assertEquals(1, result.getContractId());

        assertEquals(0.0, result.getLow());
        assertEquals(1.0, result.getOpen());
        assertEquals(1.0, result.getClose());
        assertEquals(2.0, result.getHigh());
        assertEquals(BigDecimal.valueOf(100), result.getVolume());

        verify(ibkrTimeStampFormatter, times(1)).formatStringToTimeStamp(bar.time());
        verify(historicalRepository, times(1)).findFirstByContractIdAndTimeAndCount(1,time, bar.count());
        verify(barToHistoricalDataDbo, times(1)).convert(bar);

    }

}
