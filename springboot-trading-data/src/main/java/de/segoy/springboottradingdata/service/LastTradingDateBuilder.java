package de.segoy.springboottradingdata.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@Slf4j
@RequiredArgsConstructor
public class LastTradingDateBuilder {

    public String getDateStringFromDate(LocalDate date){
        return  String.valueOf(date.getYear()) +date.getMonth() + date.getDayOfMonth();
    }
    public String getDateStringFromToday(){
        return getDateStringFromDate(LocalDate.now());
    }
}
