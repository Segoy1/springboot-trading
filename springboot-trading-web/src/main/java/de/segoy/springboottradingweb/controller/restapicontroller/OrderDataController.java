package de.segoy.springboottradingweb.controller.restapicontroller;

import de.segoy.springboottradingdata.model.OrderData;
import de.segoy.springboottradingdata.repository.OrderDataRepository;
import de.segoy.springboottradingdata.type.Action;
import de.segoy.springboottradingdata.type.OrderType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/order/order_data")
public class OrderDataController {

    private final OrderDataRepository orderDataRepository;

    public OrderDataController(OrderDataRepository orderDataRepository) {
        this.orderDataRepository = orderDataRepository;
    }
    @PutMapping("market_order")
    public  ResponseEntity<OrderData> marketOrderData(@RequestParam(name = "action")Action action,
                                                      @RequestParam(name = "quantity")int quantity) {
        OrderData orderData = OrderData.builder()
                .orderType(OrderType.MKT)
                .totalQuantity(new BigDecimal(quantity))
                .action(action).build();

        OrderData savedOrderData = orderDataRepository.save(orderData);
        return ResponseEntity.ok(savedOrderData);
    }

    @PutMapping("limit_order")
    public  ResponseEntity<OrderData> limitOrderData(@RequestParam(name = "action")Action action,
                                                     @RequestParam(name = "quantity") int quantity,
                                                     @RequestParam(name = "limitPrice") String limitPrice){


        OrderData orderData = OrderData.builder()
                .orderType(OrderType.LMT)
                .limitPrice(new BigDecimal(limitPrice))
                .totalQuantity(new BigDecimal(quantity))
                .action(action).build();

        OrderData savedOrderData = orderDataRepository.save(orderData);
        return ResponseEntity.ok(savedOrderData);
    }

    @PutMapping("/data_object")
    public ResponseEntity<OrderData> setOrderData(@RequestBody OrderData orderData){
        OrderData savedOrderData = orderDataRepository.save(orderData);
        return ResponseEntity.ok(savedOrderData);
    }
}
