package de.segoy.springboottradingweb.controller.restapicontroller;

import de.segoy.springboottradingdata.model.ContractData;
import de.segoy.springboottradingdata.model.OrderData;
import de.segoy.springboottradingibkr.client.services.OrderExecutionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class OrderController {

    private final OrderDataController orderDataController;
    private final ContractDataController contractDataController;
    private final OrderExecutionService orderExecutionService;

    public OrderController(OrderDataController orderDataController,
                           ContractDataController contractDataController,
                           OrderExecutionService orderExecutionService) {
        this.orderDataController = orderDataController;
        this.contractDataController = contractDataController;
        this.orderExecutionService = orderExecutionService;
    }

    @PutMapping
    public ResponseEntity<ContractData> orderWithExitingContractAndOrder(@RequestParam("contractDataId") int contractDataId,
                                                           @RequestParam("orderDataId")int orderDataId){

        ResponseEntity<ContractData> contractDataEntity = contractDataController.getContractDataById(contractDataId);
        ResponseEntity<OrderData> orderDataEntity = orderDataController.getContractDataById(orderDataId);

        if(contractDataEntity.getStatusCode().is2xxSuccessful()&& orderDataEntity.getStatusCode().is2xxSuccessful()){
            return executeOrder(contractDataEntity.getBody(), orderDataEntity.getBody());

        }else{
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<ContractData> orderWithContractAndOrderObject(ContractData contractData, OrderData orderData){
        return executeOrder(contractData, orderData);
    }

    private ResponseEntity<ContractData> executeOrder(ContractData contractData, OrderData orderData){

        ResponseEntity<ContractData> contractResponse = contractDataController.saveContractData(contractData);
        ResponseEntity<OrderData> orderResponse = orderDataController.saveOrderData(orderData);

        if(contractResponse.getStatusCode().is2xxSuccessful() && orderResponse.getStatusCode().is2xxSuccessful()){
        orderExecutionService.executeOrder(contractData,orderData);
        return ResponseEntity.ok(contractData);
        }else{
            return ResponseEntity.badRequest().build();
        }
    }
}