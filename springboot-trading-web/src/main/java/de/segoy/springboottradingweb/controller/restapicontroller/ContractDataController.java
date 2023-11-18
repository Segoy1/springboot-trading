package de.segoy.springboottradingweb.controller.restapicontroller;

import com.ib.client.Types;
import de.segoy.springboottradingdata.model.ComboLegData;
import de.segoy.springboottradingdata.model.ContractData;
import de.segoy.springboottradingdata.repository.ContractDataRepository;
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
                .lastTradeDateOrContractMonth(lastTradeDate)
                .strike(new BigDecimal(strike))
                .right(right)
                .build();
        ContractData savedContract = contractDataRepository.save(contract);

        return ResponseEntity.ok(savedContract);
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

    @PutMapping("/four_leg")
    public ResponseEntity<ContractData> fourLegContractDataWithParams(@RequestParam(defaultValue = "SPX", name = "symbol") String symbol,
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
                                                                      @RequestParam(defaultValue = "CBOE", name = "leg2exchange") String leg2Exchange,
                                                                      @RequestParam(name = "leg3conid") int leg3ContractId,
                                                                      @RequestParam(defaultValue = "1", name = "leg3ratio") int leg3Ratio,
                                                                      @RequestParam(defaultValue = "SELL", name = "leg3action") Types.Action leg3Action,
                                                                      @RequestParam(defaultValue = "CBOE", name = "leg3exchange") String leg3Exchange,
                                                                      @RequestParam(name="leg4conid") int leg4ContractId,
                                                                      @RequestParam(defaultValue = "1", name="leg4ratio") int leg4Ratio,
                                                                      @RequestParam(defaultValue = "BUY", name="leg4action") Types.Action leg4Action,
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
    public ResponseEntity<ContractData> saveContractData(@RequestBody ContractData contract) {
        ContractData savedContract = contractDataRepository.save(contract);
        return ResponseEntity.ok(savedContract);
    }

    @GetMapping
    public ResponseEntity<ContractData> getContractDataById(@RequestParam("id") int id){

        return contractDataRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(()-> ResponseEntity.notFound().build());
    }
}
