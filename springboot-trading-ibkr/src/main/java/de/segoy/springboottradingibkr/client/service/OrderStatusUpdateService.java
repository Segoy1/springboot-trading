package de.segoy.springboottradingibkr.client.service;

import com.ib.client.OrderStatus;
import de.segoy.springboottradingdata.model.OrderData;
import de.segoy.springboottradingdata.repository.OrderDataRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderStatusUpdateService {

    OrderDataRepository orderDataRepository;

    public OrderStatusUpdateService(OrderDataRepository orderDataRepository) {
        this.orderDataRepository = orderDataRepository;
    }

    public OrderData updateOrderStatus(int orderId, String status) {
        OrderData orderData = orderDataRepository.findById((long)orderId).orElseThrow();
        orderData.setStatus(OrderStatus.get(status));
        return orderDataRepository.save(orderData);
    }
}
