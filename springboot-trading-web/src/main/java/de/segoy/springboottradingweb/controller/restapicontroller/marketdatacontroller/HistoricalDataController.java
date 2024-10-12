package de.segoy.springboottradingweb.controller.restapicontroller.marketdatacontroller;

import de.segoy.springboottradingdata.dataobject.ContractDataTemplates;
import de.segoy.springboottradingdata.model.data.entity.HistoricalDataDBO;
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
    public ResponseEntity<List<HistoricalDataDBO>> testHistoricalData(@RequestParam(name = "barSize")String barSize, @RequestParam(name = "duration")String duration) {
        HistoricalDataSettings settings = HistoricalDataSettings.builder()
                .contractDataDBO(ContractDataTemplates.SpxData())
                .barSizeSetting(BarSizeSetting.get(barSize))
                .backfillDuration(duration)
                .backfillEndTime(Timestamp.from(Instant.now()))
                .whatToShow(WhatToShowType.TRADES)
                .regularTradingHours(true)
                .dateFormatStyle(2)
                .keepUpToDate(false)
                .build();
        List<HistoricalDataDBO> historicalDatumDBOS = historicalDataService.requestHistoricalData(settings);
        return responseMapper.mapResponse(historicalDatumDBOS);
    }
    @GetMapping("/25yearsSP")
    public ResponseEntity<List<HistoricalDataDBO>> getLast25YearsofSP500() {
        HistoricalDataSettings settings = HistoricalDataSettings.builder()
                .contractDataDBO(ContractDataTemplates.SpxData())
                .barSizeSetting(BarSizeSetting.ONE_DAY)
                .backfillDuration("25 Y")
                .backfillEndTime(Timestamp.from(Instant.now()))
                .whatToShow(WhatToShowType.TRADES)
                .regularTradingHours(true)
                .dateFormatStyle(2)
                .keepUpToDate(false)
                .build();
        List<HistoricalDataDBO> historicalDatumDBOS = historicalDataService.requestHistoricalData(settings);
        return responseMapper.mapResponse(historicalDatumDBOS);
    }
    @GetMapping("/25yearsNASDAQ")
    public ResponseEntity<List<HistoricalDataDBO>> getLast25YearsofNASDAQ() {
        HistoricalDataSettings settings = HistoricalDataSettings.builder()
                .contractDataDBO(ContractDataTemplates.NasDaqData())
                .barSizeSetting(BarSizeSetting.ONE_DAY)
                .backfillDuration("25 Y")
                .backfillEndTime(Timestamp.from(Instant.now()))
                .whatToShow(WhatToShowType.TRADES)
                .regularTradingHours(true)
                .dateFormatStyle(2)
                .keepUpToDate(false)
                .build();
        List<HistoricalDataDBO> historicalDatumDBOS = historicalDataService.requestHistoricalData(settings);
        return responseMapper.mapResponse(historicalDatumDBOS);
    }
    @GetMapping("/25yearsXOI")
    public ResponseEntity<List<HistoricalDataDBO>> getLast25YearsofOilIndex() {
        HistoricalDataSettings settings = HistoricalDataSettings.builder()
                .contractDataDBO(ContractDataTemplates.XOIData())
                .barSizeSetting(BarSizeSetting.ONE_DAY)
                .backfillDuration("25 Y")
                .backfillEndTime(Timestamp.from(Instant.now()))
                .whatToShow(WhatToShowType.TRADES)
                .regularTradingHours(true)
                .dateFormatStyle(2)
                .keepUpToDate(false)
                .build();
        List<HistoricalDataDBO> historicalDatumDBOS = historicalDataService.requestHistoricalData(settings);
        return responseMapper.mapResponse(historicalDatumDBOS);
    }

    @GetMapping("/25yearsVIX")
    public ResponseEntity<List<HistoricalDataDBO>> getLast25YearsofVIX() {
        HistoricalDataSettings settings = HistoricalDataSettings.builder()
                .contractDataDBO(ContractDataTemplates.VIXData())
                .barSizeSetting(BarSizeSetting.ONE_DAY)
                .backfillDuration("25 Y")
                .backfillEndTime(Timestamp.from(Instant.now()))
                .whatToShow(WhatToShowType.TRADES)
                .regularTradingHours(true)
                .dateFormatStyle(2)
                .keepUpToDate(false)
                .build();
        List<HistoricalDataDBO> historicalDatumDBOS = historicalDataService.requestHistoricalData(settings);
        return responseMapper.mapResponse(historicalDatumDBOS);
    }
    @GetMapping("/25yearsTLT")
    public ResponseEntity<List<HistoricalDataDBO>> getLast25YearsofTLT() {
        HistoricalDataSettings settings = HistoricalDataSettings.builder()
                .contractDataDBO(ContractDataTemplates.TLTData())
                .barSizeSetting(BarSizeSetting.ONE_DAY)
                .backfillDuration("25 Y")
                .backfillEndTime(Timestamp.from(Instant.now()))
                .whatToShow(WhatToShowType.TRADES)
                .regularTradingHours(true)
                .dateFormatStyle(2)
                .keepUpToDate(false)
                .build();
        List<HistoricalDataDBO> historicalDatumDBOS = historicalDataService.requestHistoricalData(settings);
        return responseMapper.mapResponse(historicalDatumDBOS);
    }

    @PutMapping
    public ResponseEntity<List<HistoricalDataDBO>> requestHistoricalData(@Valid @RequestBody HistoricalDataSettings settings){
        return responseMapper.mapResponse(historicalDataService.requestHistoricalData(settings));
    }
}
