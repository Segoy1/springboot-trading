package de.segoy.springboottradingibkr.client.service;

import com.ib.client.OrderStatus;
import de.segoy.springboottradingdata.model.data.entity.OrderDbo;
import de.segoy.springboottradingdata.repository.OrderRepository;
import de.segoy.springboottradingibkr.client.service.order.OrderStatusUpdateService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderStatusUpdateServiceTest {

    @Mock
    OrderRepository orderRepository;
    @InjectMocks
    OrderStatusUpdateService orderStatusUpdateService;

    @Test
    void testStatusUpdateWithExistingOrderAndInvalidStatus(){
        Long id = 1L;
        String status = "default";
        OrderDbo orderData = OrderDbo.builder().id(id).status(OrderStatus.get(status)).build();

        when(orderRepository.findById(id)).thenReturn(Optional.of(orderData));

        orderStatusUpdateService.updateOrderStatus(id.intValue(), status);

        assertEquals(1, orderData.getId());
        assertEquals("Unknown", orderData.getStatus().name());
    }

    @Test
    void testStatusUpdateWithExistingOrderAndValidStatus(){
        Long id = 1L;
        String status = "PreSubmitted";
        OrderDbo orderData = OrderDbo.builder().id(id).status(OrderStatus.get(status)).build();

        when(orderRepository.findById(id)).thenReturn(Optional.of(orderData));

        orderStatusUpdateService.updateOrderStatus(id.intValue(), status);

        assertEquals(1, orderData.getId());
        assertEquals("PreSubmitted", orderData.getStatus().name());
    }
    @Test
    void testStatusUpdateWithNotOrder(){
        Long id = 1L;
        String status = "unknown";
        OrderDbo orderData = OrderDbo.builder().id(id).build();

        when(orderRepository.findById(id)).thenReturn(Optional.empty());

        Exception e = assertThrows(NoSuchElementException.class, () -> {
            orderStatusUpdateService.updateOrderStatus(id.intValue(), status);
        });
        String expected = "No value present";

        assertTrue(e.getMessage().contains(expected));

    }
}
