package de.segoy.springboottradingibkr.client.service.order.ordercancel;

import com.ib.client.OrderStatus;
import de.segoy.springboottradingdata.model.OrderData;
import de.segoy.springboottradingdata.repository.OrderDataRepository;
import org.springframework.stereotype.Service;

@Service
class OrderCancelDeleteService {

    private final OrderDataRepository orderDataRepository;

    public OrderCancelDeleteService(OrderDataRepository orderDataRepository) {
        this.orderDataRepository = orderDataRepository;
    }

    public void deleteCancelled(OrderData orderData) {
        if (orderData.getStatus().equals(OrderStatus.Cancelled)) {
            orderDataRepository.delete(orderData);
        }
    }
}
