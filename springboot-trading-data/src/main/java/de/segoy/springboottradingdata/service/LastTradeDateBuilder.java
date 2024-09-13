package de.segoy.springboottradingdata.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@Slf4j
@RequiredArgsConstructor
public class LastTradeDateBuilder {

    public String getDateStringFromDate(LocalDate date){
        int month = date.getMonth().getValue();
        String monthAsString = month < 10 ? "0" + month : month + "";
        return  date.getYear() + monthAsString + date.getDayOfMonth();
    }
    public String getDateStringFromToday(){
        return getDateStringFromDate(LocalDate.now());
    }
    public int getDateIntFromToday(){
        return Integer.parseInt(getDateStringFromToday());
    }
}
