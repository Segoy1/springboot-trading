package de.segoy.springboottradingweb.controller.restapicontroller;

import com.ib.client.Types;
import de.segoy.springboottradingdata.model.ContractData;
import de.segoy.springboottradingdata.repository.ContractDataRepository;
import de.segoy.springboottradingibkr.client.services.UniqueContractDataProvider;
import de.segoy.springboottradingibkr.client.strategybuilder.StrategyBuilderService;
import de.segoy.springboottradingibkr.client.strategybuilder.type.Leg;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/contract")
public class ContractDataController {

    private final ContractDataRepository contractDataRepository;
    private final UniqueContractDataProvider uniqueContractDataProvider;
    private final StrategyBuilderService strategyBuilderService;

    public ContractDataController(ContractDataRepository contractDataRepository,
                                  UniqueContractDataProvider uniqueContractDataProvider,
                                  StrategyBuilderService strategyBuilderService) {
        this.contractDataRepository = contractDataRepository;
        this.uniqueContractDataProvider = uniqueContractDataProvider;
        this.strategyBuilderService = strategyBuilderService;
    }

    //Test Code
    @GetMapping("/iron_condor")
    public ResponseEntity<ContractData> fourLegContractDataWithParams(@RequestParam(defaultValue = "SPX", name = "symbol") String symbol,
                                                                      @RequestParam(defaultValue = "BAG", name = "securityType") Types.SecType securityType,
                                                                      @RequestParam(defaultValue = "USD", name = "currency") String currency,
                                                                      @RequestParam(defaultValue = "SMART", name = "exchange") String exchange,
                                                                      @RequestParam(defaultValue = "SPXW", name = "tradingClass") String tradingClass,
                                                                      @RequestParam(required = true, name = "lastTradeDate") String lastTradeDate,
                                                                      @RequestParam(required = true, name = "buyPutStrike") double buyPutStrike,
                                                                      @RequestParam(required = true, name = "sellPutStrike") double sellPutStrike,
                                                                      @RequestParam(required = true, name = "buyCallStrike") double buyCallStrike,
                                                                      @RequestParam(required = true, name = "sellCallStrike") double sellCallStrike
                                                                      ) {

        Map<Leg, Double> legs = new HashMap<>();
        legs.put(Leg.BUY_PUT_ONE, buyPutStrike);
        legs.put(Leg.SELL_PUT_ONE, sellPutStrike);
        legs.put(Leg.BUY_CALL_ONE, buyCallStrike);
        legs.put(Leg.SELL_CALL_ONE,sellCallStrike);

        ContractData contract = ContractData.builder()
                .symbol(symbol)
                .securityType(securityType)
                .currency(currency)
                .exchange(exchange)
                .lastTradeDate(lastTradeDate)
                .build();

        ContractData savedContract = strategyBuilderService.getComboLegContractData(contract, legs);

        return ResponseEntity.ok(savedContract);
    }


    @PutMapping()
    public ResponseEntity<ContractData> saveContractData(@RequestBody ContractData contract) {
        ContractData savedContract = uniqueContractDataProvider.getExistingContractDataOrCallApi(contract);
        return ResponseEntity.ok(savedContract);
    }

    @GetMapping
    public ResponseEntity<ContractData> getContractDataById(@RequestParam("id") int id) {

        return contractDataRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
