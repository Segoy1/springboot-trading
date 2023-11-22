package de.segoy.springboottradingweb.controller.restapicontroller;

import com.ib.client.Types;
import de.segoy.springboottradingdata.model.ComboLegData;
import de.segoy.springboottradingdata.model.ContractData;
import de.segoy.springboottradingdata.repository.ContractDataRepository;
import de.segoy.springboottradingibkr.client.services.UniqueContractDataProvider;
import de.segoy.springboottradingibkr.client.strategybuilder.IronCondorService;
import de.segoy.springboottradingibkr.client.strategybuilder.type.Leg;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/contract")
public class ContractDataController {

    private final ContractDataRepository contractDataRepository;
    private final UniqueContractDataProvider uniqueContractDataProvider;
    private final IronCondorService ironCondorService;

    public ContractDataController(ContractDataRepository contractDataRepository,
                                  UniqueContractDataProvider uniqueContractDataProvider,
                                  IronCondorService ironCondorService) {
        this.contractDataRepository = contractDataRepository;
        this.uniqueContractDataProvider = uniqueContractDataProvider;
        this.ironCondorService = ironCondorService;
    }

    @RequestMapping("/single")
    public ResponseEntity<ContractData> singleLegContractDataWithMinimalParams(@RequestParam(defaultValue = "SPX", name = "symbol", required = false) String symbol,
                                                                               @RequestParam(defaultValue = "OPT", name = "securityType", required = false) Types.SecType securityType,
                                                                               @RequestParam(defaultValue = "USD", name = "currency", required = false) String currency,
                                                                               @RequestParam(defaultValue = "SMART", name = "exchange", required = false) String exchange,
                                                                               @RequestParam(name = "date") String lastTradeDate,
                                                                               @RequestParam(name = "strike") String strike,
                                                                               @RequestParam(name = "right") Types.Right right) {
        ContractData contract = ContractData.builder()
                .symbol(symbol)
                .securityType(securityType)
                .currency(currency)
                .exchange(exchange)
                .lastTradeDate(lastTradeDate)
                .strike(new BigDecimal(strike))
                .right(right)
                .build();
        ContractData updatedContract = uniqueContractDataProvider.getExistingContractDataOrCallApi(contract);
        return ResponseEntity.ok(updatedContract);
    }


    @PutMapping("/two_leg")
    public ResponseEntity<ContractData> twoLegContractDataWithParams(@RequestParam(defaultValue = "SPX", name = "symbol") String symbol,
                                                                     @RequestParam(defaultValue = "BAG", name = "securityType") Types.SecType securityType,
                                                                     @RequestParam(defaultValue = "USD", name = "currency") String currency,
                                                                     @RequestParam(defaultValue = "SMART", name = "exchange") String exchange,
                                                                     @RequestParam(name = "leg1conid") int leg1ContractId,
                                                                     @RequestParam(defaultValue = "1", name = "leg1ratio") int leg1Ratio,
                                                                     @RequestParam(defaultValue = "BUY", name = "leg1action") Types.Action leg1Action,
                                                                     @RequestParam(defaultValue = "CBOE", name = "leg1exchange") String leg1Exchange,
                                                                     @RequestParam(name = "leg2conid") int leg2ContractId,
                                                                     @RequestParam(defaultValue = "1", name = "leg2ratio") int leg2Ratio,
                                                                     @RequestParam(defaultValue = "SELL", name = "leg2action") Types.Action leg2Action,
                                                                     @RequestParam(defaultValue = "CBOE", name = "leg2exchange") String leg2Exchange) {
        List<ComboLegData> legs = new ArrayList<>();
        ComboLegData leg1 = ComboLegData.builder()
                .contractId(leg1ContractId)
                .ratio(leg1Ratio)
                .action(leg1Action)
                .exchange(leg1Exchange)
                .build();

        ComboLegData leg2 = ComboLegData.builder()
                .contractId(leg2ContractId)
                .ratio(leg2Ratio)
                .action(leg2Action)
                .exchange(leg2Exchange)
                .build();
        legs.add(leg1);
        legs.add(leg2);

        ContractData contract = ContractData.builder()
                .symbol(symbol)
                .securityType(securityType)
                .currency(currency)
                .exchange(exchange)
                .comboLegs(legs)
                .build();

        ContractData savedContract = contractDataRepository.save(contract);
        return ResponseEntity.ok(savedContract);
    }

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
        legs.put(Leg.BUY_PUT, buyPutStrike);
        legs.put(Leg.SELL_PUT, sellPutStrike);
        legs.put(Leg.BUY_CALL, buyCallStrike);
        legs.put(Leg.SELL_CALL,sellCallStrike);

        ContractData contract = ContractData.builder()
                .symbol(symbol)
                .securityType(securityType)
                .currency(currency)
                .exchange(exchange)
                .lastTradeDate(lastTradeDate)
                .build();

        ContractData savedContract = ironCondorService.getIronCondorContractData(contract, legs);

        return ResponseEntity.ok(savedContract);
    }


    @PutMapping("/data_object")
    public ResponseEntity<ContractData> saveContractData(@RequestBody ContractData contract) {
        ContractData savedContract = contractDataRepository.save(contract);
        return ResponseEntity.ok(savedContract);
    }

    @GetMapping
    public ResponseEntity<ContractData> getContractDataById(@RequestParam("id") int id) {

        return contractDataRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
