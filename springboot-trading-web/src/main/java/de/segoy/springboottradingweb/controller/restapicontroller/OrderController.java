package de.segoy.springboottradingweb.controller.restapicontroller;

import com.ib.client.OrderType;
import com.ib.client.Types;
import de.segoy.springboottradingdata.config.PropertiesConfig;
import de.segoy.springboottradingdata.dataobject.ContractDataTemplates;
import de.segoy.springboottradingdata.model.entity.OrderData;
import de.segoy.springboottradingdata.repository.ContractDataRepository;
import de.segoy.springboottradingibkr.client.service.order.OrderService;
import de.segoy.springboottradingibkr.client.service.order.openorders.OpenOrdersService;
import de.segoy.springboottradingibkr.client.service.order.ordercancel.OrderCancelService;
import de.segoy.springboottradingweb.service.ResponseMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {

    private final PropertiesConfig propertiesConfig;
    private final OrderService orderService;
    private final ResponseMapper responseMapper;
    private final OrderCancelService orderCancelService;
    private final OpenOrdersService openOrdersService;

    //only for test purposes
    private final ContractDataRepository contractDataRepository;


    public OrderController(PropertiesConfig propertiesConfig, OrderService orderService, ResponseMapper responseMapper, OrderCancelService orderCancelService, OpenOrdersService openOrdersService, ContractDataRepository contractDataRepository) {
        this.propertiesConfig = propertiesConfig;
        this.orderService = orderService;
        this.responseMapper = responseMapper;
        this.orderCancelService = orderCancelService;
        this.openOrdersService = openOrdersService;
        this.contractDataRepository = contractDataRepository;
    }
    @GetMapping("/test")
    public ResponseEntity<OrderData> test(){
        OrderData orderData = OrderData.builder()
                .action(Types.Action.BUY)
                .totalQuantity(BigDecimal.ONE)
                .orderType(OrderType.LMT)
                .limitPrice(BigDecimal.valueOf(400))
                .contractData(ContractDataTemplates.SpxData())
                .build();
        return responseMapper.mapResponse(orderService.setIdAndPlaceOrder(orderData));
    }

    @GetMapping("/test2")
    public ResponseEntity<OrderData> testWithParam(@RequestParam(name = "contractId") int contractId){
        OrderData orderData = OrderData.builder()
                .action(Types.Action.BUY)
                .totalQuantity(BigDecimal.ONE)
                .orderType(OrderType.MKT)
                .contractData(contractDataRepository.findById((long)contractId).get())
                .build();
        return responseMapper.mapResponse(orderService.setIdAndPlaceOrder(orderData));
    }

//    valid Test Statements:
//    curl -X PUT http://localhost:8080/order/place-order
//      -H 'Cookie: JSESSIONID=F455B73BA89F45584DAC5B3EEE14812C'
//      -H 'Content-Type: application/json'
//      -d '{"action": "BUY","totalQuantity": 1,"orderType": "LMT","limitPrice": 10,"auctionPrice": 0,"timeInForce": "DAY","cashQuantity": 0,"usePriceManagementAlgorithm": false,"contractData": {"id": 9000000,"contractId": 666788906,"symbol": "SPX","securityType": "OPT","currency": "USD","exchange": "SMART","lastTradeDate": "20231204","strike": 4595,"right": "Call","multiplier": "100","localSymbol": "SPXW  231204C04595000","tradingClass": "SPXW","includeExpired": false,"comboLegsDescription": null,"comboLegs": []}}'

    //    curl -i -X POST 'http://localhost:8080/login' --data 'username=john&password=john'
    @PutMapping("/place-order")
    public ResponseEntity<OrderData> orderWithOrderObject(@RequestBody OrderData orderData) {
        orderData.setId(propertiesConfig.getNextValidOrderId());
        return responseMapper.mapResponse(orderService.setIdAndPlaceOrder(orderData));
    }

    @DeleteMapping("delete-order")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteOrderById(Long id){
        orderCancelService.cancelOrderbyId(id);
    }

    @DeleteMapping("delete-all-open-orders")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteAllOpenOrders(){
        orderCancelService.cancelAllOpenOrders();
    }

    @GetMapping("/open-orders")
    public ResponseEntity<List<OrderData>> getOpenOrders(){
        return responseMapper.mapResponse(openOrdersService.getOpenOrders());
    }
}