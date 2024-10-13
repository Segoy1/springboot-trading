package de.segoy.springboottradingibkr.client.service.order;

import com.ib.client.OrderStatus;
import de.segoy.springboottradingdata.model.data.entity.OrderDbo;
import de.segoy.springboottradingdata.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderStatusUpdateService {

    private final OrderRepository orderRepository;

    public OrderDbo updateOrderStatus(int orderId, String status) {
        OrderDbo orderData = orderRepository.findById((long) orderId).orElseThrow();
        if (OrderStatus.get(status).equals(OrderStatus.Cancelled) ||
                OrderStatus.get(status).equals(OrderStatus.ApiCancelled)) {
            orderRepository.delete(orderData);
            return orderData;
        } else {
            orderData.setStatus(OrderStatus.get(status));
            return orderRepository.save(orderData);
        }
    }
}
