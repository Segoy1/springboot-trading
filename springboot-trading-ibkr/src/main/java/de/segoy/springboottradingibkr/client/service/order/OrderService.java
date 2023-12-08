package de.segoy.springboottradingibkr.client.service.order;

import de.segoy.springboottradingdata.model.OrderData;
import de.segoy.springboottradingdata.repository.OrderDataRepository;
import de.segoy.springboottradingdata.service.ApiResponseInEntityChecker;
import de.segoy.springboottradingibkr.client.config.PropertiesConfig;
import de.segoy.springboottradingibkr.client.service.contract.ContractDataValidator;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrderService {

    private final OrderDataRepository orderDataRepository;
    private final ContractDataValidator contractDataValidator;
    private final OrderPlacementService orderPlacementService;
    private final ApiResponseInEntityChecker apiResponseInEntityChecker;
    private final PropertiesConfig propertiesConfig;

    public OrderService(OrderDataRepository orderDataRepository, ContractDataValidator contractDataValidator, OrderPlacementService orderPlacementService, ApiResponseInEntityChecker apiResponseInEntityChecker, PropertiesConfig propertiesConfig) {
        this.orderDataRepository = orderDataRepository;
        this.contractDataValidator = contractDataValidator;
        this.orderPlacementService = orderPlacementService;
        this.apiResponseInEntityChecker = apiResponseInEntityChecker;
        this.propertiesConfig = propertiesConfig;
    }


    public Optional<OrderData> validateAndPlaceOrder(OrderData orderData) {
        if (orderData.getId() == null) {
            orderData.setId(propertiesConfig.getNextValidOrderId());
        }
        if (contractDataValidator.validate(orderData)) {
            orderPlacementService.placeOrder(orderData);
            return apiResponseInEntityChecker.checkForApiResponseAndUpdate(orderDataRepository, orderData.getId());
        }
        return Optional.empty();
    }
}
