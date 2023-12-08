package de.segoy.springboottradingweb.controller.restapicontroller.marketdatacontroller;

import de.segoy.springboottradingdata.dataobject.ContractDataTemplates;
import de.segoy.springboottradingdata.model.ContractData;
import de.segoy.springboottradingibkr.client.service.MarketDataService;
import de.segoy.springboottradingweb.service.ResponseMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/market-data")
public class LiveMarketDataController {

    private final MarketDataService marketDataService;
    private final ResponseMapper responseMapper;

    public LiveMarketDataController(MarketDataService marketDataService, ResponseMapper responseMapper) {
        this.marketDataService = marketDataService;
        this.responseMapper = responseMapper;
    }

    //TestCode to be deleted later on
    @GetMapping("/test")
    public ResponseEntity<ContractData> handleRealTimeDataTest(){

        return responseMapper.mapResponse(marketDataService.requestLiveMarketDataForContractData(ContractDataTemplates.SpxData()));

    }
    @GetMapping
    public ResponseEntity<ContractData> handleRealTimeData(@RequestBody ContractData contractData){
        return responseMapper.mapResponse(marketDataService.requestLiveMarketDataForContractData(contractData));

    }
}
