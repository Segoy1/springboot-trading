package de.segoy.springboottradingweb.controller.restapicontroller;

import de.segoy.springboottradingdata.model.OrderData;
import de.segoy.springboottradingdata.repository.OrderDataRepository;
import de.segoy.springboottradingibkr.client.services.OrderPlacementService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/order")
public class OrderController {

    private final OrderDataRepository orderDataRepository;
    private final ContractDataController contractDataController;
    private final OrderPlacementService orderPlacementService;

    public OrderController(OrderDataRepository orderDataRepository,
                           ContractDataController contractDataController,
                           OrderPlacementService orderPlacementService) {
        this.orderDataRepository = orderDataRepository;
        this.contractDataController = contractDataController;
        this.orderPlacementService = orderPlacementService;
    }

    @PutMapping("/placeExistingOrder")
    public ResponseEntity<OrderData> orderWithExistingId(@RequestParam("orderDataId") int orderDataId) {

        Optional<OrderData> orderDataOptional = orderDataRepository.findById(orderDataId);
        return orderDataOptional.map(this::executeOrder).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/placeOrder")
    public ResponseEntity<OrderData> orderWithOrderObject(@Valid @RequestBody OrderData orderData) {
        OrderData savedOrder = orderDataRepository.save(orderData);
        return executeOrder(savedOrder);
    }

    private ResponseEntity<OrderData> executeOrder(OrderData orderData) {
            orderPlacementService.placeOrder(orderData);
            orderData.setPlaced(true);
            OrderData savedAndPlacedOrder = orderDataRepository.save(orderData);
            return ResponseEntity.ok(savedAndPlacedOrder);
    }
}