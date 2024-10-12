package de.segoy.springboottradingdata.service;

import com.ib.client.Contract;
import com.ib.client.Order;
import com.ib.client.OrderStatus;
import de.segoy.springboottradingdata.model.data.entity.ContractDataDBO;
import de.segoy.springboottradingdata.model.data.entity.OrderDataDBO;
import de.segoy.springboottradingdata.modelconverter.IBKROrderToOrderData;
import de.segoy.springboottradingdata.modelsynchronize.ContractDataDatabaseSynchronizer;
import de.segoy.springboottradingdata.repository.OrderDataRepository;
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
    private OrderDataRepository orderDataRepository;
    @Mock
    private IBKROrderToOrderData ibkrOrderToOrderData;
    @Mock
    private ContractDataDatabaseSynchronizer contractDataDatabaseSynchronizer;
    @InjectMocks
    private OrderWriteToDBService orderWriteToDBService;

    @Test
    void testSaveOrUpdateFullOrderDataToDb(){

        Order order = new Order();
        Contract contract = new Contract();
        ContractDataDBO contractDataDBO = ContractDataDBO.builder().build();
        OrderDataDBO orderData = OrderDataDBO.builder().build();

        when(contractDataDatabaseSynchronizer.findInDBOrConvertAndSaveOrUpdateIfIdIsProvided(
                OptionalLong.empty(), contract)).thenReturn(contractDataDBO);
        when(ibkrOrderToOrderData.convertOrder(order)).thenReturn(orderData);
        when(orderDataRepository.save(orderData)).thenReturn(orderData);

        OrderDataDBO result = orderWriteToDBService.saveOrUpdateFullOrderDataToDb(order, contract,
                OrderStatus.Filled.toString());


        assertEquals(OrderStatus.Filled, result.getStatus());
        assertEquals(contractDataDBO, result.getContractDataDBO());

        verify(contractDataDatabaseSynchronizer,times(1)).findInDBOrConvertAndSaveOrUpdateIfIdIsProvided(
                OptionalLong.empty(), contract);
        verify(ibkrOrderToOrderData, times(1)).convertOrder(order);
        verify(orderDataRepository, times(1)).save(orderData);
    }
}
