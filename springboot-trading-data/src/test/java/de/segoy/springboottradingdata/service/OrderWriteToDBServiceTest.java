package de.segoy.springboottradingdata.service;

import com.ib.client.Contract;
import com.ib.client.Order;
import com.ib.client.OrderStatus;
import de.segoy.springboottradingdata.model.data.entity.ContractDbo;
import de.segoy.springboottradingdata.model.data.entity.OrderDbo;
import de.segoy.springboottradingdata.modelconverter.IBKRToOrderDbo;
import de.segoy.springboottradingdata.modelsynchronize.ContractDataDatabaseSynchronizer;
import de.segoy.springboottradingdata.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.OptionalLong;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class OrderWriteToDBServiceTest {


    @Mock
    private OrderRepository orderRepository;
    @Mock
    private IBKRToOrderDbo ibkrToOrderDbo;
    @Mock
    private ContractDataDatabaseSynchronizer contractDataDatabaseSynchronizer;
    @InjectMocks
    private OrderWriteToDBService orderWriteToDBService;

    @Test
    void testSaveOrUpdateFullOrderDataToDb(){

        Order order = new Order();
        Contract contract = new Contract();
        ContractDbo contractDBO = ContractDbo.builder().build();
        OrderDbo orderData = OrderDbo.builder().build();

        when(contractDataDatabaseSynchronizer.findInDBOrConvertAndSaveOrUpdateIfIdIsProvided(
                OptionalLong.empty(), contract)).thenReturn(contractDBO);
        when(ibkrToOrderDbo.convertOrder(order)).thenReturn(orderData);
        when(orderRepository.save(orderData)).thenReturn(orderData);

        OrderDbo result = orderWriteToDBService.saveOrUpdateFullOrderDataToDb(order, contract,
                OrderStatus.Filled.toString());


        assertEquals(OrderStatus.Filled, result.getStatus());
        assertEquals(contractDBO, result.getContractDBO());

        verify(contractDataDatabaseSynchronizer,times(1)).findInDBOrConvertAndSaveOrUpdateIfIdIsProvided(
                OptionalLong.empty(), contract);
        verify(ibkrToOrderDbo, times(1)).convertOrder(order);
        verify(orderRepository, times(1)).save(orderData);
    }
}
