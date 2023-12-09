package de.segoy.springboottradingweb.controller.restapicontroller.marketdatacontroller;

import de.segoy.springboottradingdata.dataobject.ContractDataTemplates;
import de.segoy.springboottradingdata.model.HistoricalMarketData;
import de.segoy.springboottradingibkr.client.datamodel.HistoricalDataSettings;
import de.segoy.springboottradingibkr.client.datamodel.subtype.WhatToShowType;
import de.segoy.springboottradingibkr.client.service.historicalmarketdata.HistoricalMarketDataService;
import de.segoy.springboottradingweb.service.ResponseMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.List;

@RestController
@RequestMapping("historical-market-data")
public class HistoricalMarketDataController {

    private final HistoricalMarketDataService historicalMarketDataService;
    private final ResponseMapper responseMapper;

    public HistoricalMarketDataController(HistoricalMarketDataService historicalMarketDataService, ResponseMapper responseMapper) {
        this.historicalMarketDataService = historicalMarketDataService;
        this.responseMapper = responseMapper;
    }

    @GetMapping("/Test")
    public ResponseEntity<List<HistoricalMarketData>> testHistoricalData() {
        HistoricalDataSettings settings = HistoricalDataSettings.builder()
                .barSizeSetting("1 day")
                .backfillDuration("1 Y")
                .backfillEndTime(Timestamp.valueOf("2023-12-08 09:00:00"))
                .whatToShow(WhatToShowType.TRADES)
                .regularTradingHours(true)
                .dateFormatStyle(1)
                .keepUpToDate(false)
                .build();
        List<HistoricalMarketData> historicalData = historicalMarketDataService.requestHistoricalData(ContractDataTemplates.SpxData(), settings);
        return responseMapper.mapResponse(historicalData);
    }
}
