package de.segoy.springboottradingweb.controller.livedatacontroller;

import com.ib.client.EClientSocket;
import com.ib.client.Types;
import de.segoy.springboottradingdata.model.ContractData;
import de.segoy.springboottradingdata.modelconverter.ContractDataToIBKRContract;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/market-data")
public class RealTimeBarsController {

    private final EClientSocket client;
    private final ContractDataToIBKRContract contractDataToIBKRContract;

    public RealTimeBarsController(EClientSocket client, ContractDataToIBKRContract contractDataToIBKRContract) {
        this.client = client;
        this.contractDataToIBKRContract = contractDataToIBKRContract;
    }

    @GetMapping("/test")
    public void handleRealTimeDataTest(){
        ContractData contractData = ContractData.builder().securityType(Types.SecType.CASH).symbol("EUR").exchange("IDEALPRO").currency("GBP").build();
        client.reqMktData(30,
                contractDataToIBKRContract.convertContractData(contractData),
                "100, 101, 104",
                false,
                false,
                null);

    }
    @GetMapping
    public void handleRealTimeData(@RequestBody ContractData contractData){
        client.reqMktData(contractData.getId(),
                contractDataToIBKRContract.convertContractData(contractData),
                "100, 101, 104",
                false,
                false,
                null);

    }
}
