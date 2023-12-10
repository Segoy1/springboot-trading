package de.segoy.springboottradingweb.controller.restapicontroller;

import com.ib.client.Types;
import de.segoy.springboottradingdata.model.ContractData;
import de.segoy.springboottradingdata.repository.ContractDataRepository;
import de.segoy.springboottradingdata.model.Leg;
import de.segoy.springboottradingibkr.client.service.LegMapService;
import de.segoy.springboottradingibkr.client.service.contract.UniqueContractDataProvider;
import de.segoy.springboottradingibkr.client.strategybuilder.StrategyBuilderService;
import de.segoy.springboottradingweb.service.ResponseMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/contract")
public class ContractDataController {

    private final ContractDataRepository contractDataRepository;
    private final UniqueContractDataProvider uniqueContractDataProvider;
    private final StrategyBuilderService strategyBuilderService;
    private final LegMapService legMapService;
    private final ResponseMapper responseMapper;

    public ContractDataController(ContractDataRepository contractDataRepository,
                                  UniqueContractDataProvider uniqueContractDataProvider,
                                  StrategyBuilderService strategyBuilderService, LegMapService legMapService, ResponseMapper responseMapper) {
        this.contractDataRepository = contractDataRepository;
        this.uniqueContractDataProvider = uniqueContractDataProvider;
        this.strategyBuilderService = strategyBuilderService;
        this.legMapService = legMapService;
        this.responseMapper = responseMapper;
    }

    @PutMapping("/combo")
    public ResponseEntity<ContractData> ComboLegContractData(@RequestBody ContractData contractData,
                                                             @RequestParam(defaultValue = "0", name = "buyPutStrike") double buyPutStrike,
                                                             @RequestParam(defaultValue = "0", name = "sellPutStrike") double sellPutStrike,
                                                             @RequestParam(defaultValue = "0", name = "buyCallStrike") double buyCallStrike,
                                                             @RequestParam(defaultValue = "0", name = "sellCallStrike") double sellCallStrike,
                                                             @RequestParam(defaultValue = "0", name = "buyPutStrikeTwo") double buyPutStrikeTwo,
                                                             @RequestParam(defaultValue = "0", name = "sellPutStrikeTwo") double sellPutStrikeTwo,
                                                             @RequestParam(defaultValue = "0", name = "buyCallStrikeTwo") double buyCallStrikeTwo,
                                                             @RequestParam(defaultValue = "0", name = "sellCallStrikeTwo") double sellCallStrikeTwo) {



        Optional<ContractData> savedContract = strategyBuilderService.getComboLegContractData(contractData,  legMapService.mapLegs(buyPutStrike,
                sellPutStrike,
                buyCallStrike,
                sellCallStrike,
                buyPutStrikeTwo,
                sellPutStrikeTwo,
                buyCallStrikeTwo,
                sellCallStrikeTwo));

        return responseMapper.mapResponse(savedContract);
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

        List<Leg> legs = new ArrayList<>();
        legs.add(Leg.builder().action(Types.Action.BUY).right(Types.Right.Put).strike(buyPutStrike).ratio(1).build());
        legs.add(Leg.builder().action(Types.Action.SELL).right(Types.Right.Put).strike(sellPutStrike).ratio(1).build());
        legs.add(Leg.builder().action(Types.Action.BUY).right(Types.Right.Call).strike(buyCallStrike).ratio(1).build());
        legs.add(Leg.builder().action(Types.Action.SELL).right(Types.Right.Call).strike(sellCallStrike).ratio(1).build());

        ContractData contractData = ContractData.builder()
                .symbol(symbol)
                .securityType(securityType)
                .currency(currency)
                .exchange(exchange)
                .lastTradeDate(lastTradeDate)
                .build();

        Optional<ContractData> savedContract = strategyBuilderService.getComboLegContractData(contractData, legs);

        return responseMapper.mapResponse(savedContract);
    }


    @PutMapping()
    public ResponseEntity<ContractData> saveContractData(@RequestBody ContractData contractData) {
        Optional<ContractData> savedContract = uniqueContractDataProvider.getExistingContractDataOrCallApi(contractData);
        return responseMapper.mapResponse(savedContract);
    }

    @GetMapping
    public ResponseEntity<ContractData> getContractDataById(@RequestParam("id") long id) {

        return responseMapper.mapResponse(contractDataRepository.findById(id));
    }
}
