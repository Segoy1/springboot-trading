package de.segoy.springboottradingweb.controller.restapicontroller.marketdatacontroller;

import de.segoy.springboottradingdata.dataobject.ContractDataTemplates;
import de.segoy.springboottradingdata.model.HistoricalData;
import de.segoy.springboottradingdata.model.HistoricalDataSettings;
import de.segoy.springboottradingdata.model.subtype.BarSizeSetting;
import de.segoy.springboottradingdata.model.subtype.WhatToShowType;
import de.segoy.springboottradingibkr.client.service.historicaldata.HistoricalDataService;
import de.segoy.springboottradingweb.service.ResponseMapper;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;

@RestController
@RequestMapping("historical-data")
public class HistoricalDataController {

    private final HistoricalDataService historicalDataService;
    private final ResponseMapper responseMapper;

    public HistoricalDataController(HistoricalDataService historicalDataService, ResponseMapper responseMapper) {
        this.historicalDataService = historicalDataService;
        this.responseMapper = responseMapper;
    }

    @GetMapping("/Test")
    public ResponseEntity<List<HistoricalData>> testHistoricalData() {
        HistoricalDataSettings settings = HistoricalDataSettings.builder()
                .contractData(ContractDataTemplates.SpxData())
                .barSizeSetting(BarSizeSetting.get("1 day"))
                .backfillDuration("1 Y")
                .backfillEndTime(Timestamp.valueOf("2023-12-08 09:00:00"))
                .whatToShow(WhatToShowType.TRADES)
                .regularTradingHours(true)
                .dateFormatStyle(1)
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
