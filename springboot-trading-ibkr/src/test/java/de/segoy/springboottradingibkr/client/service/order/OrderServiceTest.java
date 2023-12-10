package de.segoy.springboottradingibkr.client.service.order;

import de.segoy.springboottradingdata.config.PropertiesConfig;
import de.segoy.springboottradingdata.model.OrderData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderValidateAndPlacementService orderValidateAndPlacementService;
    @Mock
    private PropertiesConfig propertiesConfig;
    @InjectMocks
    private OrderService orderService;

    @Test
    void testSetIdAndPlaceOrderOnAbsent(){
        OrderData orderData = OrderData.builder().build();
        when(propertiesConfig.getNextValidOrderId()).thenReturn(1L);
        when(orderValidateAndPlacementService.validateAndPlaceOrder(orderData)).thenReturn(Optional.empty());

        assertTrue(orderService.setIdAndPlaceOrder(orderData).isEmpty());
    }
    @Test
    void testSetIdAndPlaceOrderOnPresent(){
        OrderData orderData = OrderData.builder().build();
        when(propertiesConfig.getNextValidOrderId()).thenReturn(1L);
        when(orderValidateAndPlacementService.validateAndPlaceOrder(orderData)).thenReturn(Optional.of(orderData));

        Optional<OrderData>opt =orderService.setIdAndPlaceOrder(orderData);
        assertTrue(opt.isPresent());
        assertEquals(1L,opt.get().getId());
    }

}