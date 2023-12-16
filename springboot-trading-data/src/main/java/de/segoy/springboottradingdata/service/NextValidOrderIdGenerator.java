package de.segoy.springboottradingdata.service;

import de.segoy.springboottradingdata.config.PropertiesConfig;
import de.segoy.springboottradingdata.repository.OrderDataRepository;
import org.springframework.stereotype.Service;

@Service
public class NextValidOrderIdGenerator {

    private final PropertiesConfig propertiesConfig;
    private final OrderDataRepository orderDataRepository;


    public NextValidOrderIdGenerator(PropertiesConfig propertiesConfig, OrderDataRepository orderDataRepository) {
        this.propertiesConfig = propertiesConfig;
        this.orderDataRepository = orderDataRepository;
    }

    public long generateAndSaveNextOrderId(int id) {
        propertiesConfig.setNextValidOrderId(orderDataRepository.findTopByOrderByIdDesc().map(
                orderData -> orderData.getId() > id ? orderData.getId() + 1 : id).orElse((long)id));
        return propertiesConfig.getNextValidOrderId();
    }
}
