package de.segoy.springboottradingdata.service;

import de.segoy.springboottradingdata.config.PropertiesConfig;
import de.segoy.springboottradingdata.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NextValidOrderIdGenerator {

    private final PropertiesConfig propertiesConfig;
    private final OrderRepository orderRepository;


    public long generateAndSaveNextOrderId(int id) {
        long oldId = propertiesConfig.getNextValidOrderId();
        long newId = orderRepository.findTopByOrderByIdDesc().map(
                orderData -> orderData.getId() >= id ? orderData.getId() + 1 : id).orElse((long)id);
        propertiesConfig.setNextValidOrderId(Math.max(oldId, newId));
        return propertiesConfig.getNextValidOrderId();
    }
}
