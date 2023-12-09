package de.segoy.springboottradingdata.service;

import de.segoy.springboottradingdata.config.PropertiesConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@Slf4j
public class IBKRTimeStampFormatter {

    private final PropertiesConfig propertiesConfig;

    public IBKRTimeStampFormatter(PropertiesConfig propertiesConfig) {
        this.propertiesConfig = propertiesConfig;
    }

    public String formatTimestampToDateAndTime(Timestamp timestamp) {
        return new SimpleDateFormat(propertiesConfig.getDateTimeFormat()).format(timestamp);
    }

    public String formatTimestampToDate(Timestamp timestamp) {
        //TODO: see if this is actually needed
        return new SimpleDateFormat(propertiesConfig.getDateFormat()).format(timestamp);
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
        } else {
            String format = propertiesConfig.getDateTimeFormat();
            return Timestamp.valueOf(LocalDateTime.from(DateTimeFormatter.ofPattern(format).parse(timeString)));
        }
    }
}
