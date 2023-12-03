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
import java.util.Optional;

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

    @PutMapping
    public ResponseEntity<ContractData> ComboLegContractData(@RequestBody ContractData contractData,
                                                             @RequestParam(defaultValue = "0", name = "buyPutStrike") double buyPutStrike,
                                                             @RequestParam(defaultValue = "0", name = "sellPutStrike") double sellPutStrike,
                                                             @RequestParam(defaultValue = "0", name = "buyCallStrike") double buyCallStrike,
                                                             @RequestParam(defaultValue = "0", name = "sellCallStrike") double sellCallStrike,
                                                             @RequestParam(defaultValue = "0", name = "buyPutStrikeTwo") double buyPutStrikeTwo,
                                                             @RequestParam(defaultValue = "0", name = "sellPutStrikeTwo") double sellPutStrikeTwo,
                                                             @RequestParam(defaultValue = "0", name = "buyCallStrikeTwo") double buyCallStrikeTwo,
                                                             @RequestParam(defaultValue = "0", name = "sellCallStrikeTwo") double sellCallStrikeTwo) {

        Map<Leg, Double> legMap = populateLegMap(buyPutStrike,
                sellPutStrike,
                buyCallStrike,
                sellCallStrike,
                buyPutStrikeTwo,
                sellPutStrikeTwo,
                buyCallStrikeTwo,
                sellCallStrikeTwo);

        Optional<ContractData> savedContract = strategyBuilderService.getComboLegContractData(contractData, legMap);

        return savedContract.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
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
        legs.put(Leg.SELL_CALL_ONE, sellCallStrike);

        ContractData contractData = ContractData.builder()
                .symbol(symbol)
                .securityType(securityType)
                .currency(currency)
                .exchange(exchange)
                .lastTradeDate(lastTradeDate)
                .build();

        Optional<ContractData> savedContract = strategyBuilderService.getComboLegContractData(contractData, legs);

        return savedContract.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }


    @PutMapping()
    public ResponseEntity<ContractData> saveContractData(@RequestBody ContractData contractData) {
        Optional<ContractData> savedContract = uniqueContractDataProvider.getExistingContractDataOrCallApi(contractData);
        return savedContract.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @GetMapping
    public ResponseEntity<ContractData> getContractDataById(@RequestParam("id") int id) {

        return contractDataRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    private Map<Leg, Double> populateLegMap(double buyPutStrike, double sellPutStrike, double buyCallStrike, double sellCallStrike, double buyPutStrikeTwo, double sellPutStrikeTwo, double buyCallStrikeTwo, double sellCallStrikeTwo) {
        Map<Leg, Double> legs = new HashMap<>();
        if (buyPutStrike != 0) {
            legs.put(Leg.BUY_PUT_ONE, buyPutStrike);
        }
        if (sellPutStrike != 0) {
            legs.put(Leg.SELL_PUT_ONE, sellPutStrike);
        }
        if (buyCallStrike != 0) {
            legs.put(Leg.BUY_CALL_ONE, buyCallStrike);
        }
        if (sellCallStrike != 0) {
            legs.put(Leg.SELL_CALL_ONE, sellCallStrike);
        }
        if (buyPutStrikeTwo != 0) {
            legs.put(Leg.BUY_PUT_TWO, buyPutStrikeTwo);
        }
        if (sellPutStrikeTwo != 0) {
            legs.put(Leg.SELL_PUT_TWO, sellPutStrikeTwo);
        }
        if (buyCallStrikeTwo != 0) {
            legs.put(Leg.BUY_CALL_TWO, buyCallStrikeTwo);
        }
        if (sellCallStrikeTwo != 0) {
            legs.put(Leg.SELL_CALL_TWO, sellCallStrikeTwo);
        }
        return legs;
    }
}
