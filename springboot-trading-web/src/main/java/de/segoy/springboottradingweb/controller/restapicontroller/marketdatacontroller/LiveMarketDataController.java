package de.segoy.springboottradingweb.controller.restapicontroller.marketdatacontroller;

import de.segoy.springboottradingdata.model.data.entity.ContractData;
import de.segoy.springboottradingibkr.client.service.marketdata.StartMarketDataService;
import de.segoy.springboottradingibkr.client.service.marketdata.StopMarketDataService;
import de.segoy.springboottradingweb.service.ResponseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/market-data")
@RequiredArgsConstructor
public class LiveMarketDataController {

    private final StartMarketDataService startMarketDataService;
    private final StopMarketDataService stopMarketDataService;
    private final ResponseMapper responseMapper;

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
