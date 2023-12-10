package de.segoy.springboottradingweb.controller.restapicontroller.marketdatacontroller;

import de.segoy.springboottradingdata.dataobject.ContractDataTemplates;
import de.segoy.springboottradingdata.model.ContractData;
import de.segoy.springboottradingibkr.client.service.marketdata.StartMarketDataService;
import de.segoy.springboottradingweb.service.ResponseMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/market-data")
public class LiveMarketDataController {

    private final StartMarketDataService startMarketDataService;
    private final ResponseMapper responseMapper;

    public LiveMarketDataController(StartMarketDataService startMarketDataService, ResponseMapper responseMapper) {
        this.startMarketDataService = startMarketDataService;
        this.responseMapper = responseMapper;
    }

    //TestCode to be deleted later on
    @GetMapping("/test")
    public ResponseEntity<ContractData> startRealTimeDataTest(){

        return responseMapper.mapResponse(startMarketDataService.requestLiveMarketDataForContractData(ContractDataTemplates.SpxData()));

    }
    @GetMapping("/start")
    public ResponseEntity<ContractData> startRealTimeData(@RequestBody ContractData contractData){
        return responseMapper.mapResponse(startMarketDataService.requestLiveMarketDataForContractData(contractData));
    }

    @GetMapping("/stop")
    public ResponseEntity<ContractData> stopRealTimeData(@RequestBody ContractData contractData) {
        return responseMapper.mapResponse(Optional.of(contractData));
    }
}
