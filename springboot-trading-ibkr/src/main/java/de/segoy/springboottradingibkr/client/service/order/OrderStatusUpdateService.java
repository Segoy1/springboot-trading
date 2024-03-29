package de.segoy.springboottradingibkr.client.service.order;

import com.ib.client.OrderStatus;
import de.segoy.springboottradingdata.model.data.entity.OrderData;
import de.segoy.springboottradingdata.repository.OrderDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderStatusUpdateService {

    private final OrderDataRepository orderDataRepository;

    public OrderData updateOrderStatus(int orderId, String status) {
        OrderData orderData = orderDataRepository.findById((long) orderId).orElseThrow();
        if (OrderStatus.get(status).equals(OrderStatus.Cancelled) ||
                OrderStatus.get(status).equals(OrderStatus.ApiCancelled)) {
            orderDataRepository.delete(orderData);
            return orderData;
        } else {
            orderData.setStatus(OrderStatus.get(status));
            return orderDataRepository.save(orderData);
        }
    }
}
