package de.segoy.springboottradingweb.controller.restapicontroller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class OrderController {

    private final OrderDataController orderDataController;
    private final ContractDataController contractDataController;

    public OrderController(OrderDataController orderDataController, ContractDataController contractDataController) {
        this.orderDataController = orderDataController;
        this.contractDataController = contractDataController;
    }

    public void orderWithExitingContractAndOrder(){

    }
}
