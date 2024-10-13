package de.segoy.springboottradingdata.service;

import de.segoy.springboottradingdata.config.PropertiesConfig;
import de.segoy.springboottradingdata.model.data.entity.OrderDbo;
import de.segoy.springboottradingdata.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NextValidOrderIdGeneratorTest {

    @Mock
    private PropertiesConfig propertiesConfig;
    @Mock
    private OrderRepository orderRepository;
    @InjectMocks
    private NextValidOrderIdGenerator nextValidOrderIdGenerator;

    @Test
    void testGenerateNextOrderIdFromOrder(){
        OrderDbo orderData = OrderDbo.builder().id(20L).build();

        when(orderRepository.findTopByOrderByIdDesc()).thenReturn(Optional.of(orderData));
        when(propertiesConfig.getNextValidOrderId()).thenReturn(19L,21L);

        Long result = nextValidOrderIdGenerator.generateAndSaveNextOrderId(18);

        assertEquals(21L, result);
        verify(propertiesConfig, times(1)).setNextValidOrderId(21L);
    }
    @Test
    void testGenerateNextOrderIdFromProperties(){
        OrderDbo orderData = OrderDbo.builder().id(18L).build();

        when(orderRepository.findTopByOrderByIdDesc()).thenReturn(Optional.of(orderData));
        when(propertiesConfig.getNextValidOrderId()).thenReturn(19L);

        nextValidOrderIdGenerator.generateAndSaveNextOrderId(18);

        verify(propertiesConfig, times(1)).setNextValidOrderId(19L);
    }
    @Test
    void testGenerateNextOrderIdFromParsedId(){
        OrderDbo orderData = OrderDbo.builder().id(20L).build();

        when(orderRepository.findTopByOrderByIdDesc()).thenReturn(Optional.of(orderData));
        when(propertiesConfig.getNextValidOrderId()).thenReturn(19L, 42L);

        Long result = nextValidOrderIdGenerator.generateAndSaveNextOrderId(42);

        assertEquals(42L, result);
        verify(propertiesConfig, times(1)).setNextValidOrderId(42L);
    }

}
