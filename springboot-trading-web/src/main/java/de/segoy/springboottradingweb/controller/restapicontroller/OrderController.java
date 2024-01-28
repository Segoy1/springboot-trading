package de.segoy.springboottradingweb.controller.restapicontroller;

import de.segoy.springboottradingdata.model.data.StrategyOrderData;
import de.segoy.springboottradingdata.model.data.entity.OrderData;
import de.segoy.springboottradingibkr.client.service.order.OrderPlacementService;
import de.segoy.springboottradingibkr.client.service.order.openorders.OpenOrdersService;
import de.segoy.springboottradingibkr.client.service.order.ordercancel.OrderCancelService;
import de.segoy.springboottradingibkr.client.strategybuilder.StrategyOrderDataBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
public class OrderController {

    private final StrategyOrderDataBuilder strategyOrderDataBuilder;
    private final OrderPlacementService orderPlacementService;
    private final OrderCancelService orderCancelService;
    private final OpenOrdersService openOrdersService;

    public OrderController(StrategyOrderDataBuilder strategyOrderDataBuilder, OrderPlacementService orderPlacementService,
                           OrderCancelService orderCancelService,
                           OpenOrdersService openOrdersService) {
        this.strategyOrderDataBuilder = strategyOrderDataBuilder;
        this.orderPlacementService = orderPlacementService;
        this.orderCancelService = orderCancelService;
        this.openOrdersService = openOrdersService;
    }

    //    valid Test Statements:
//    curl -X PUT http://localhost:8080/order/place-order
//      -H 'Cookie: JSESSIONID=F455B73BA89F45584DAC5B3EEE14812C'
//      -H 'Content-Type: application/json'
//      -d '{"action": "BUY","totalQuantity": 1,"orderType": "LMT","limitPrice": 10,"auctionPrice": 0,"timeInForce":
//      "DAY","cashQuantity": 0,"usePriceManagementAlgorithm": false,"contractData": {"id": 9000000,"contractId":
//      666788906,"symbol": "SPX","securityType": "OPT","currency": "USD","exchange": "SMART","lastTradeDate":
//      "20231204","strike": 4595,"right": "Call","multiplier": "100","localSymbol": "SPXW  231204C04595000",
//      "tradingClass": "SPXW","includeExpired": false,"comboLegsDescription": null,"comboLegs": []}}'

    //    curl -i -X POST 'http://localhost:8080/login' --data 'username=john&password=john'
    @PostMapping("/place-order")
    public void orderWithOrderObject(@RequestBody OrderData orderData) {
        orderPlacementService.setIdAndPlaceOrder(orderData);
    }

    @PostMapping("/place-strategy-order")
    public void orderWithStrategyOrderObject(@RequestBody StrategyOrderData strategyOrderData) {
        strategyOrderDataBuilder.buildOrderWithStrategyData(strategyOrderData).ifPresent(
                orderPlacementService::setIdAndPlaceOrder
        );
    }

    @DeleteMapping("/cancel-order")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void cancelOrderById(@RequestParam(name = "id") int id) {
        orderCancelService.cancelOrderbyId((long) id);
    }

    @DeleteMapping("/cancel-all-open-orders")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void cancelAllOpenOrders() {
        orderCancelService.cancelAllOpenOrders();
    }

    @GetMapping("/open-orders")
    public void requestOpenOrders() {
        openOrdersService.requestOpenOrders();
    }
}
