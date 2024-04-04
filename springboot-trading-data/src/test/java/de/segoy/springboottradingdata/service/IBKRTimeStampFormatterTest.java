package de.segoy.springboottradingdata.service;

import de.segoy.springboottradingdata.config.PropertiesConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IBKRTimeStampFormatterTest {

    @Mock
    private PropertiesConfig propertiesConfig;
    @InjectMocks
    private IBKRTimeStampFormatter ibkrTimeStampFormatter;


    @Test
    void testDate() {

        when(propertiesConfig.getDateFormat()).thenReturn("yyyyMMdd");
        when(propertiesConfig.getDateTimeFormat()).thenReturn("yyyyMMdd-HH:mm:ss");

        Timestamp result =  ibkrTimeStampFormatter.formatStringToTimeStamp("20240404");

        assertEquals("20240404-00:00:00", ibkrTimeStampFormatter.formatTimestampToDateAndTime(result));

    }

    @Test
    void testDateTime() {

        when(propertiesConfig.getDateTimeFormat()).thenReturn("yyyyMMdd-HH:mm:ss");

        Timestamp result =  ibkrTimeStampFormatter.formatStringToTimeStamp("20240404-23:58:23");

        assertEquals("20240404-23:58:23", ibkrTimeStampFormatter.formatTimestampToDateAndTime(result));
    }
    @Test
    void testTimestamp() {

        when(propertiesConfig.getDateTimeFormat()).thenReturn("yyyyMMdd-HH:mm:ss");
        when(propertiesConfig.getAddMillis()).thenReturn("000");

        Timestamp result =  ibkrTimeStampFormatter.formatStringToTimeStamp("1712267903");

        assertEquals("20240404-23:58:23", ibkrTimeStampFormatter.formatTimestampToDateAndTime(result));
    }



}
