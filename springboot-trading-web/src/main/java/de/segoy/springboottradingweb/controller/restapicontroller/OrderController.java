package de.segoy.springboottradingweb.controller.restapicontroller;

import de.segoy.springboottradingdata.model.OrderData;
import de.segoy.springboottradingdata.repository.ContractDataRepository;
import de.segoy.springboottradingdata.repository.OrderDataRepository;
import de.segoy.springboottradingdata.service.ApiResponseInEntityChecker;
import de.segoy.springboottradingibkr.client.services.ContractDataValidator;
import de.segoy.springboottradingibkr.client.services.OrderPlacementService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/order")
public class OrderController {

    private final OrderDataRepository orderDataRepository;
    private final OrderPlacementService orderPlacementService;
    private final ApiResponseInEntityChecker apiResponseInEntityChecker;
    private final NextAvailableOrderIdController nextAvailableOrderIdController;
    private final ContractDataValidator contractDataValidator;

    private final ContractDataRepository contractDataRepository;

    public OrderController(OrderDataRepository orderDataRepository,
                           OrderPlacementService orderPlacementService, ApiResponseInEntityChecker apiResponseInEntityChecker, NextAvailableOrderIdController nextAvailableOrderIdController, ContractDataValidator contractDataValidator, ContractDataRepository contractDataRepository) {
        this.orderDataRepository = orderDataRepository;
        this.orderPlacementService = orderPlacementService;
        this.apiResponseInEntityChecker = apiResponseInEntityChecker;
        this.nextAvailableOrderIdController = nextAvailableOrderIdController;
        this.contractDataValidator = contractDataValidator;
        this.contractDataRepository = contractDataRepository;
    }

    @PutMapping("/place-order")
    public ResponseEntity<OrderData> orderWithOrderObject(@RequestBody OrderData orderData) {
        orderData.setId(nextAvailableOrderIdController.getNextAvailableOrderId());
        return executeOrder(orderData);
    }

    private ResponseEntity<OrderData> executeOrder(OrderData orderData) {

        if(contractDataValidator.validate(orderData)){
        orderPlacementService.placeOrder(orderData);
        Optional<OrderData> savedAndPlacedOrder = apiResponseInEntityChecker.checkForApiResponseAndUpdate(orderDataRepository, orderData.getId());
        return savedAndPlacedOrder.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
        }
        return ResponseEntity.badRequest().build();
    }
//    valid Test Statements:
//    curl -X PUT http://localhost:8080/order/place-order
//      -H 'Cookie: JSESSIONID=F455B73BA89F45584DAC5B3EEE14812C'
//      -H 'Content-Type: application/json'
//      -d '{"action": "BUY","totalQuantity": 1,"orderType": "LMT","limitPrice": 10,"auctionPrice": 0,"timeInForce": "DAY","cashQuantity": 0,"usePriceManagementAlgorithm": false,"contractData": {"id": 9000000,"contractId": 666788906,"symbol": "SPX","securityType": "OPT","currency": "USD","exchange": "SMART","lastTradeDate": "20231204","strike": 4595,"right": "Call","multiplier": "100","localSymbol": "SPXW  231204C04595000","tradingClass": "SPXW","includeExpired": false,"comboLegsDescription": null,"comboLegs": []}}'

//    curl -i -X POST 'http://localhost:8080/login' --data 'username=john&password=john'

}