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
import java.time.Instant;
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
                .backfillEndTime(Timestamp.from(Instant.now()))
                .whatToShow(WhatToShowType.TRADES)
                .regularTradingHours(true)
                .dateFormatStyle(2)
                .keepUpToDate(false)
                .build();
        List<HistoricalData> historicalData = historicalDataService.requestHistoricalData(settings);
        return responseMapper.mapResponse(historicalData);
    }
    @GetMapping("/25yearsSP")
    public ResponseEntity<List<HistoricalData>> getLast25YearsofSP500() {
        HistoricalDataSettings settings = HistoricalDataSettings.builder()
                .contractData(ContractDataTemplates.SpxData())
                .barSizeSetting(BarSizeSetting.ONE_DAY)
                .backfillDuration("25 Y")
                .backfillEndTime(Timestamp.from(Instant.now()))
                .whatToShow(WhatToShowType.TRADES)
                .regularTradingHours(true)
                .dateFormatStyle(2)
                .keepUpToDate(false)
                .build();
        List<HistoricalData> historicalData = historicalDataService.requestHistoricalData(settings);
        return responseMapper.mapResponse(historicalData);
    }
    @GetMapping("/25yearsNASDAQ")
    public ResponseEntity<List<HistoricalData>> getLast25YearsofNASDAQ() {
        HistoricalDataSettings settings = HistoricalDataSettings.builder()
                .contractData(ContractDataTemplates.NasDaqData())
                .barSizeSetting(BarSizeSetting.ONE_DAY)
                .backfillDuration("25 Y")
                .backfillEndTime(Timestamp.from(Instant.now()))
                .whatToShow(WhatToShowType.TRADES)
                .regularTradingHours(true)
                .dateFormatStyle(2)
                .keepUpToDate(false)
                .build();
        List<HistoricalData> historicalData = historicalDataService.requestHistoricalData(settings);
        return responseMapper.mapResponse(historicalData);
    }
    @GetMapping("/25yearsXOI")
    public ResponseEntity<List<HistoricalData>> getLast25YearsofOilIndex() {
        HistoricalDataSettings settings = HistoricalDataSettings.builder()
                .contractData(ContractDataTemplates.XOIData())
                .barSizeSetting(BarSizeSetting.ONE_DAY)
                .backfillDuration("25 Y")
                .backfillEndTime(Timestamp.from(Instant.now()))
                .whatToShow(WhatToShowType.TRADES)
                .regularTradingHours(true)
                .dateFormatStyle(2)
                .keepUpToDate(false)
                .build();
        List<HistoricalData> historicalData = historicalDataService.requestHistoricalData(settings);
        return responseMapper.mapResponse(historicalData);
    }

    @GetMapping("/25yearsVIX")
    public ResponseEntity<List<HistoricalData>> getLast25YearsofVIX() {
        HistoricalDataSettings settings = HistoricalDataSettings.builder()
                .contractData(ContractDataTemplates.VIXData())
                .barSizeSetting(BarSizeSetting.ONE_DAY)
                .backfillDuration("25 Y")
                .backfillEndTime(Timestamp.from(Instant.now()))
                .whatToShow(WhatToShowType.TRADES)
                .regularTradingHours(true)
                .dateFormatStyle(2)
                .keepUpToDate(false)
                .build();
        List<HistoricalData> historicalData = historicalDataService.requestHistoricalData(settings);
        return responseMapper.mapResponse(historicalData);
    }
    @GetMapping("/25yearsTLT")
    public ResponseEntity<List<HistoricalData>> getLast25YearsofTLT() {
        HistoricalDataSettings settings = HistoricalDataSettings.builder()
                .contractData(ContractDataTemplates.TLTData())
                .barSizeSetting(BarSizeSetting.ONE_DAY)
                .backfillDuration("25 Y")
                .backfillEndTime(Timestamp.from(Instant.now()))
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
