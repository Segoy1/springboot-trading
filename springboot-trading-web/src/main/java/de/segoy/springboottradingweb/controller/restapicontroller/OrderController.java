package de.segoy.springboottradingweb.controller.restapicontroller;

import com.ib.client.EClientSocket;
import de.segoy.springboottradingdata.model.ContractData;
import de.segoy.springboottradingdata.model.OrderData;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class OrderController {

    private final OrderDataController orderDataController;
    private final ContractDataController contractDataController;
    private final EClientSocket client;

    public OrderController(OrderDataController orderDataController, ContractDataController contractDataController, EClientSocket client) {
        this.orderDataController = orderDataController;
        this.contractDataController = contractDataController;
        this.client = client;
    }

    public ResponseEntity orderWithExitingContractAndOrder(@RequestParam("ContractDataId") int contractDataId,
                                                           @RequestParam("orderDataId")int orderDataId){

        ResponseEntity<ContractData> contractDataEntity = contractDataController.getContractDataById(contractDataId);
        ResponseEntity<OrderData> orderDataEntity = orderDataController.getContractDataById(orderDataId);

        if(contractDataEntity.getStatusCode().is2xxSuccessful()&& orderDataEntity.getStatusCode().is2xxSuccessful()){
            return executeOrder(contractDataEntity.getBody(), orderDataEntity.getBody());

        }else{
            return ResponseEntity.notFound().build();
        }
    }

    private ResponseEntity executeOrder(ContractData contractData, OrderData orderData){
        return null;
    }
}