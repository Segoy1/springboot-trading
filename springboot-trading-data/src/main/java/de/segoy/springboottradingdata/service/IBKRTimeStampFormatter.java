package de.segoy.springboottradingdata.service;

import de.segoy.springboottradingdata.config.PropertiesConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@Slf4j
@RequiredArgsConstructor
public class IBKRTimeStampFormatter {

    private final PropertiesConfig propertiesConfig;

    public String formatTimestampToDateAndTime(Timestamp timestamp) {
        return new SimpleDateFormat(propertiesConfig.getDateTimeFormat()).format(timestamp);
    }

    public Timestamp formatStringToTimeStamp(String timeString) {
        if (timeString.length() == 8) {
            String format = propertiesConfig.getDateFormat();
            try{
            return new Timestamp(new SimpleDateFormat(format).parse(timeString).getTime());
            }catch(ParseException e){
                log.error("Parsing Exception of Date: " + timeString + "! Only Parse Date with Format yyyyMMdd");
                return null;
            }
        } else if(timeString.length()== 17){
            String format = propertiesConfig.getDateTimeFormat();
            return Timestamp.valueOf(LocalDateTime.from(DateTimeFormatter.ofPattern(format).parse(timeString)));
        } else{
            //use unix Timestamp (HistoricalDataSettings.dateFormatStyle = 2, makes everything easier
            return new Timestamp(Long.parseLong(timeString + propertiesConfig.getAddMillis()));
        }
    }
}
