package de.segoy.springboottradingdata.service;

import de.segoy.springboottradingdata.config.PropertiesConfig;
import de.segoy.springboottradingdata.repository.OrderDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NextValidOrderIdGenerator {

    private final PropertiesConfig propertiesConfig;
    private final OrderDataRepository orderDataRepository;


    public long generateAndSaveNextOrderId(int id) {
        long oldId = propertiesConfig.getNextValidOrderId();
        long newId = orderDataRepository.findTopByOrderByIdDesc().map(
                orderData -> orderData.getId() >= id ? orderData.getId() + 1 : id).orElse((long)id);
        propertiesConfig.setNextValidOrderId(Math.max(oldId, newId));
        return propertiesConfig.getNextValidOrderId();
    }
}
