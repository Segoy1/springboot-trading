package de.segoy.springboottradingweb.controller.restapicontroller;

import com.ib.client.OrderType;
import com.ib.client.Types;
import de.segoy.springboottradingdata.config.PropertiesConfig;
import de.segoy.springboottradingdata.dataobject.ContractDataTemplates;
import de.segoy.springboottradingdata.model.StrategyData;
import de.segoy.springboottradingdata.model.entity.ContractData;
import de.segoy.springboottradingdata.model.entity.OrderData;
import de.segoy.springboottradingdata.repository.ContractDataRepository;
import de.segoy.springboottradingibkr.client.service.order.OrderService;
import de.segoy.springboottradingibkr.client.service.order.openorders.OpenOrdersService;
import de.segoy.springboottradingibkr.client.service.order.ordercancel.OrderCancelService;
import de.segoy.springboottradingibkr.client.strategybuilder.StrategyBuilderService;
import de.segoy.springboottradingibkr.client.strategybuilder.StrategyOrderDataBuilder;
import de.segoy.springboottradingweb.service.ResponseMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {

    private final StrategyOrderDataBuilder strategyOrderDataBuilder;
    private final OrderService orderService;
    private final ResponseMapper responseMapper;
    private final OrderCancelService orderCancelService;
    private final OpenOrdersService openOrdersService;

    public OrderController(StrategyOrderDataBuilder strategyOrderDataBuilder, OrderService orderService,
                           ResponseMapper responseMapper, OrderCancelService orderCancelService,
                           OpenOrdersService openOrdersService) {
        this.strategyOrderDataBuilder = strategyOrderDataBuilder;
        this.orderService = orderService;
        this.responseMapper = responseMapper;
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
    public ResponseEntity<OrderData> orderWithOrderObject(@RequestBody OrderData orderData) {
        return responseMapper.mapResponse(orderService.setIdAndPlaceOrder(orderData));
    }

    @PostMapping("/place-strategy-order")
    public ResponseEntity<OrderData> orderWithStrategyOrderObject(@RequestBody StrategyData strategyData) {
        return strategyOrderDataBuilder
                .buildOrderWithStrategyData(strategyData)
                .map(
                (orderData)->{
        return responseMapper.mapResponse(orderService.setIdAndPlaceOrder(orderData));
        }
        )
                .orElseGet(()->{
                    return ResponseEntity.badRequest().build();
                });
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
    public ResponseEntity<List<OrderData>> getOpenOrders() {
        return responseMapper.mapResponse(openOrdersService.getOpenOrders());
    }
}
