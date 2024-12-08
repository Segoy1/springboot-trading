package de.segoy.springboottradingibkr.client.service.order;

import com.ib.client.OrderStatus;
import de.segoy.springboottradingdata.model.data.entity.OrderDbo;
import de.segoy.springboottradingdata.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class OrderStatusUpdateService {

    private final OrderRepository orderRepository;

    public OrderDbo updateOrderStatus(int orderId, String status) {
        return orderRepository.save(updateWithoutSave(orderId, status));
    }

    private OrderDbo updateWithoutSave(long orderId, String status) {
        OrderDbo orderData = orderRepository.findById(orderId).orElseThrow();
        if (OrderStatus.get(status).equals(OrderStatus.Cancelled) ||
                OrderStatus.get(status).equals(OrderStatus.ApiCancelled)) {
            orderRepository.delete(orderData);
        } else {
            orderData.setStatus(OrderStatus.get(status));
            orderData.setLastModifiedBeforeSave();
        }
        return orderData;
    }

    public OrderDbo updateOrderStatusWithAvgFillPrice(int orderId, String status, double avgFillPrice) {
        OrderDbo orderData = updateWithoutSave(orderId, status);
        orderData.setAvgFillPrice(BigDecimal.valueOf(avgFillPrice));
        orderData.setLastModifiedBeforeSave();
        return orderRepository.save(orderData);
    }
}
