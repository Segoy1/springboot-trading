package de.segoy.springboottradingweb.controller.restapicontroller;

import de.segoy.springboottradingdata.model.OrderData;
import de.segoy.springboottradingibkr.client.service.OrderService;
import de.segoy.springboottradingweb.service.ResponseMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class OrderController {

    private final NextAvailableOrderIdController nextAvailableOrderIdController;
    private final OrderService orderService;
    private final ResponseMapper responseMapper;


    public OrderController(NextAvailableOrderIdController nextAvailableOrderIdController, OrderService orderService, ResponseMapper responseMapper) {
        this.nextAvailableOrderIdController = nextAvailableOrderIdController;
        this.orderService = orderService;
        this.responseMapper = responseMapper;
    }

//    valid Test Statements:
//    curl -X PUT http://localhost:8080/order/place-order
//      -H 'Cookie: JSESSIONID=F455B73BA89F45584DAC5B3EEE14812C'
//      -H 'Content-Type: application/json'
//      -d '{"action": "BUY","totalQuantity": 1,"orderType": "LMT","limitPrice": 10,"auctionPrice": 0,"timeInForce": "DAY","cashQuantity": 0,"usePriceManagementAlgorithm": false,"contractData": {"id": 9000000,"contractId": 666788906,"symbol": "SPX","securityType": "OPT","currency": "USD","exchange": "SMART","lastTradeDate": "20231204","strike": 4595,"right": "Call","multiplier": "100","localSymbol": "SPXW  231204C04595000","tradingClass": "SPXW","includeExpired": false,"comboLegsDescription": null,"comboLegs": []}}'

    //    curl -i -X POST 'http://localhost:8080/login' --data 'username=john&password=john'
    @PutMapping("/place-order")
    public ResponseEntity<OrderData> orderWithOrderObject(@RequestBody OrderData orderData) {
        orderData.setId(nextAvailableOrderIdController.getNextAvailableOrderId());
        return responseMapper.mapResponse(orderService.validateAndPlaceOrder(orderData));
    }
}