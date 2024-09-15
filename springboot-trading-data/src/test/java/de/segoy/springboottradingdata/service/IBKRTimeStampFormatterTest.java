package de.segoy.springboottradingdata.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import de.segoy.springboottradingdata.config.PropertiesConfig;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
    void testTimestampToDateString() throws ParseException {

        when(propertiesConfig.getDateFormat()).thenReturn("yyyyMMdd");
        DateFormat format = new SimpleDateFormat(propertiesConfig.getDateFormat());

        Timestamp time = new Timestamp(format.parse("20240915").getTime());
        String result =  ibkrTimeStampFormatter.formatTimestampToDate(time);

        assertEquals("20240915", result);

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
