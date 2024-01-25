package de.segoy.springboottradingweb.controller.restapicontroller.marketdatacontroller;

import de.segoy.springboottradingdata.dataobject.ContractDataTemplates;
import de.segoy.springboottradingdata.model.data.entity.ContractData;
import de.segoy.springboottradingibkr.client.service.marketdata.StartMarketDataService;
import de.segoy.springboottradingibkr.client.service.marketdata.StopMarketDataService;
import de.segoy.springboottradingweb.service.ResponseMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/market-data")
public class LiveMarketDataController {

    private final StartMarketDataService startMarketDataService;
    private final StopMarketDataService stopMarketDataService;
    private final ResponseMapper responseMapper;

    public LiveMarketDataController(StartMarketDataService startMarketDataService, ResponseMapper responseMapper, StopMarketDataService stopMarketDataService) {
        this.startMarketDataService = startMarketDataService;
        this.stopMarketDataService = stopMarketDataService;
        this.responseMapper = responseMapper;
    }

    //TestCode to be deleted later on
    @GetMapping("/test")
    public ResponseEntity<ContractData> startMarketDataTest(){
        return responseMapper.mapResponse(startMarketDataService.requestLiveMarketDataForContractData(ContractDataTemplates.SpxData()));
    }
    @PostMapping("/start")
    public ResponseEntity<ContractData> startMarketData(@RequestBody ContractData contractData){
        return responseMapper.mapResponse(startMarketDataService.requestLiveMarketDataForContractData(contractData));
    }

    @GetMapping("/stopAll")
    public void stopAllMarketData() {
        stopMarketDataService.stopAllMarketData();
    }

    @GetMapping("/stop")
    public void stopMarketDatabyId(@RequestParam(name="id") int id) {
        stopMarketDataService.stopMarketDataForContractId(id);
    }
}
