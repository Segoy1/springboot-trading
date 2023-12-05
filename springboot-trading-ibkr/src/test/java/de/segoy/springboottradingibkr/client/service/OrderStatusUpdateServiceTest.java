package de.segoy.springboottradingibkr.client.service;

import com.ib.client.OrderStatus;
import de.segoy.springboottradingdata.model.OrderData;
import de.segoy.springboottradingdata.repository.OrderDataRepository;
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
    OrderDataRepository orderDataRepository;
    @InjectMocks
    OrderStatusUpdateService orderStatusUpdateService;

    @Test
    void testStatusUpdateWithExistingOrderAndInvalidStatus(){
        int id = 1;
        String status = "default";
        OrderData orderData = OrderData.builder().id(id).status(OrderStatus.get(status)).build();

        when(orderDataRepository.findById(id)).thenReturn(Optional.of(orderData));

        orderStatusUpdateService.updateOrderStatus(id, status);

        assertEquals(1, orderData.getId());
        assertEquals("Unknown", orderData.getStatus().name());
    }

    @Test
    void testStatusUpdateWithExistingOrderAndValidStatus(){
        int id = 1;
        String status = "PreSubmitted";
        OrderData orderData = OrderData.builder().id(id).status(OrderStatus.get(status)).build();

        when(orderDataRepository.findById(id)).thenReturn(Optional.of(orderData));

        orderStatusUpdateService.updateOrderStatus(id, status);

        assertEquals(1, orderData.getId());
        assertEquals("PreSubmitted", orderData.getStatus().name());
    }
    @Test
    void testStatusUpdateWithNotOrder(){
        int id = 1;
        String status = "unknown";
        OrderData orderData = OrderData.builder().id(id).build();

        when(orderDataRepository.findById(id)).thenReturn(Optional.empty());

        Exception e = assertThrows(NoSuchElementException.class, () -> {
            orderStatusUpdateService.updateOrderStatus(id, status);
        });
        String expected = "No value present";

        assertTrue(e.getMessage().contains(expected));

    }
}