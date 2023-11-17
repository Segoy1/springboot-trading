package de.segoy.springboottradingweb.controller.restapicontroller;

import de.segoy.springboottradingdata.model.ComboLegData;
import de.segoy.springboottradingdata.model.ContractData;
import de.segoy.springboottradingdata.repository.ContractDataRepository;
import de.segoy.springboottradingdata.type.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/order/contract_data")
public class ContractDataController {

    private final ContractDataRepository contractDataRepository;

    public ContractDataController(ContractDataRepository contractDataRepository) {
        this.contractDataRepository = contractDataRepository;
    }

    @RequestMapping("/single")
    public ResponseEntity<ContractData> singleLegContractDataWithParams(@RequestParam(defaultValue = "SPX", name = "symbol") Symbol symbol,
                                                                        @RequestParam(defaultValue = "OPT", name = "securityType") SecurityType securityType,
                                                                        @RequestParam(defaultValue = "USD", name = "currency") Currency currency,
                                                                        @RequestParam(defaultValue = "SMART", name = "exchange") String exchange,
                                                                        @RequestParam(name = "date") String lastTradeDate,
                                                                        @RequestParam(name = "strike") String strike,
                                                                        @RequestParam(name = "right") Right right,
                                                                        @RequestParam(defaultValue = "100", name = "multiplier") int multiplier,
                                                                        @RequestParam(defaultValue = "SPXW", name = "tradigClass") String tradingClass) {
        ContractData contract = ContractData.builder()
                .symbol(symbol)
                .securityType(securityType)
                .currency(currency)
                .exchange(exchange)
                .lastTradeDateOrContractMonth(lastTradeDate)
                .strike(new BigDecimal(strike))
                .right(right)
                .multiplier(new BigDecimal(multiplier))
                .tradingClass(tradingClass)
                .build();

        ContractData savedContract = contractDataRepository.save(contract);
        return ResponseEntity.ok(savedContract);
    }

    @RequestMapping("/two_leg")
    public ResponseEntity<ContractData> twoLegContractDataWithParams(@RequestParam(defaultValue = "SPX", name = "symbol") Symbol symbol,
                                                                     @RequestParam(defaultValue = "BAG", name = "securityType") SecurityType securityType,
                                                                     @RequestParam(defaultValue = "USD", name = "currency") Currency currency,
                                                                     @RequestParam(defaultValue = "SMART", name = "exchange") String exchange,
                                                                     @RequestParam(name = "leg1conid") int leg1ContractId,
                                                                     @RequestParam(defaultValue = "1", name = "leg1ratio") int leg1Ratio,
                                                                     @RequestParam(defaultValue = "BUY", name = "leg1action") Action leg1Action,
                                                                     @RequestParam(defaultValue = "CBOE", name = "leg1exchange") String leg1Exchange,
                                                                     @RequestParam(name = "leg2conid") int leg2ContractId,
                                                                     @RequestParam(defaultValue = "1", name = "leg2ratio") int leg2Ratio,
                                                                     @RequestParam(defaultValue = "SELL", name = "leg2action") Action leg2Action,
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

    @RequestMapping("/four_leg")
    public ResponseEntity<ContractData> fourLegContractDataWithParams(@RequestParam(defaultValue = "SPX", name = "symbol") Symbol symbol,
                                                                      @RequestParam(defaultValue = "BAG", name = "securityType") SecurityType securityType,
                                                                      @RequestParam(defaultValue = "USD", name = "currency") Currency currency,
                                                                      @RequestParam(defaultValue = "SMART", name = "exchange") String exchange,
                                                                      @RequestParam(name = "leg1conid") int leg1ContractId,
                                                                      @RequestParam(defaultValue = "1", name = "leg1ratio") int leg1Ratio,
                                                                      @RequestParam(defaultValue = "BUY", name = "leg1action") Action leg1Action,
                                                                      @RequestParam(defaultValue = "CBOE", name = "leg1exchange") String leg1Exchange,
                                                                      @RequestParam(name = "leg2conid") int leg2ContractId,
                                                                      @RequestParam(defaultValue = "1", name = "leg2ratio") int leg2Ratio,
                                                                      @RequestParam(defaultValue = "SELL", name = "leg2action") Action leg2Action,
                                                                      @RequestParam(defaultValue = "CBOE", name = "leg2exchange") String leg2Exchange,
                                                                      @RequestParam(name = "leg3conid") int leg3ContractId,
                                                                      @RequestParam(defaultValue = "1", name = "leg3ratio") int leg3Ratio,
                                                                      @RequestParam(defaultValue = "SELL", name = "leg3action") Action leg3Action,
                                                                      @RequestParam(defaultValue = "CBOE", name = "leg3exchange") String leg3Exchange,
                                                                      @RequestParam(name="leg4conid") int leg4ContractId,
                                                                      @RequestParam(defaultValue = "1", name="leg4ratio") int leg4Ratio,
                                                                      @RequestParam(defaultValue = "BUY", name="leg4action") Action leg4Action,
                                                                      @RequestParam(defaultValue = "CBOE", name = "leg4exchange") String leg4Exchange) {
        List<ComboLegData> legs = new ArrayList<>();

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


    @PutMapping("/data_object")
    public ResponseEntity<ContractData> setContractData(@RequestBody ContractData contract) {
        ContractData savedContract = contractDataRepository.save(contract);
        return ResponseEntity.ok(savedContract);
    }
}