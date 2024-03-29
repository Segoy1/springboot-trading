package de.segoy.springboottradingweb.controller.restapicontroller.marketdatacontroller;

import de.segoy.springboottradingdata.dataobject.ContractDataTemplates;
import de.segoy.springboottradingdata.model.data.entity.HistoricalData;
import de.segoy.springboottradingdata.model.data.HistoricalDataSettings;
import de.segoy.springboottradingdata.model.subtype.BarSizeSetting;
import de.segoy.springboottradingdata.model.subtype.WhatToShowType;
import de.segoy.springboottradingibkr.client.service.historicaldata.HistoricalDataService;
import de.segoy.springboottradingweb.service.ResponseMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;

@RestController
@RequestMapping("historical-data")
@RequiredArgsConstructor
public class HistoricalDataController {

    private final HistoricalDataService historicalDataService;
    private final ResponseMapper responseMapper;

    //TODO Error Handler who catches timeout with stopHistoricalData
    @GetMapping("/Test")
    public ResponseEntity<List<HistoricalData>> testHistoricalData(@RequestParam(name = "barSize")String barSize, @RequestParam(name = "duration")String duration) {
        HistoricalDataSettings settings = HistoricalDataSettings.builder()
                .contractData(ContractDataTemplates.SpxData())
                .barSizeSetting(BarSizeSetting.get(barSize))
                .backfillDuration(duration)
                .backfillEndTime(Timestamp.valueOf("2023-12-08 09:00:00"))
                .whatToShow(WhatToShowType.TRADES)
                .regularTradingHours(true)
                .dateFormatStyle(2)
                .keepUpToDate(false)
                .build();
        List<HistoricalData> historicalData = historicalDataService.requestHistoricalData(settings);
        return responseMapper.mapResponse(historicalData);
    }
    @PutMapping
    public ResponseEntity<List<HistoricalData>> requestHistoricalData(@Valid @RequestBody HistoricalDataSettings settings){
        return responseMapper.mapResponse(historicalDataService.requestHistoricalData(settings));
    }
}
