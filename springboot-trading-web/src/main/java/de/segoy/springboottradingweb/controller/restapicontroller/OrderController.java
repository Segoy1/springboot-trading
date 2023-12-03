package de.segoy.springboottradingweb.controller.restapicontroller;

import com.ib.client.OrderType;
import com.ib.client.Types;
import de.segoy.springboottradingdata.model.OrderData;
import de.segoy.springboottradingdata.repository.ContractDataRepository;
import de.segoy.springboottradingdata.repository.OrderDataRepository;
import de.segoy.springboottradingdata.service.ApiResponseInEntityChecker;
import de.segoy.springboottradingibkr.client.services.OrderPlacementService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Optional;

@RestController
@RequestMapping("/order")
public class OrderController {

    private final OrderDataRepository orderDataRepository;
    private final OrderPlacementService orderPlacementService;
    private final ApiResponseInEntityChecker apiResponseInEntityChecker;
    private final NextAvailableOrderIdController nextAvailableOrderIdController;

    private final ContractDataRepository contractDataRepository;

    public OrderController(OrderDataRepository orderDataRepository,
                           OrderPlacementService orderPlacementService, ApiResponseInEntityChecker apiResponseInEntityChecker, NextAvailableOrderIdController nextAvailableOrderIdController, ContractDataRepository contractDataRepository) {
        this.orderDataRepository = orderDataRepository;
        this.orderPlacementService = orderPlacementService;
        this.apiResponseInEntityChecker = apiResponseInEntityChecker;
        this.nextAvailableOrderIdController = nextAvailableOrderIdController;
        this.contractDataRepository = contractDataRepository;
    }
    @GetMapping
    public ResponseEntity<OrderData>test(){
        OrderData orderData = OrderData.builder()
                .action(Types.Action.BUY)
                .orderType(OrderType.LMT)
                .totalQuantity(new BigDecimal(1))
                .limitPrice(new BigDecimal(10))
                .contractData(contractDataRepository.findById(9000004).orElseThrow())
                .build();

        orderPlacementService.placeOrder(orderData);
        Optional<OrderData> savedAndPlacedOrder = apiResponseInEntityChecker.checkForApiResponseAndUpdate(orderDataRepository,orderData.getId());
        return savedAndPlacedOrder.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/place-existing-order")
    public ResponseEntity<OrderData> orderWithExistingId(@RequestParam("orderDataId") int orderDataId) {

        Optional<OrderData> orderDataOptional = orderDataRepository.findById(orderDataId);
        return orderDataOptional.map(this::executeOrder).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/place-order")
    public ResponseEntity<OrderData> orderWithOrderObject(@Valid OrderData orderData) {
        OrderData savedOrder = orderDataRepository.save(orderData);
        return executeOrder(savedOrder);
    }

    private ResponseEntity<OrderData> executeOrder(OrderData orderData) {
            orderPlacementService.placeOrder(orderData);
            Optional<OrderData> savedAndPlacedOrder = apiResponseInEntityChecker.checkForApiResponseAndUpdate(orderDataRepository,orderData.getId());
            return savedAndPlacedOrder.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}