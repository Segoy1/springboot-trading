package de.segoy.springboottradingibkr.client.service.order;

import com.ib.client.Types;
import de.segoy.springboottradingdata.model.data.entity.ContractDbo;
import de.segoy.springboottradingdata.model.data.entity.OrderDbo;
import de.segoy.springboottradingibkr.client.service.ApiCaller;
import de.segoy.springboottradingibkr.client.service.contract.UniqueContractDataProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class OrderValidateAndPlacementServiceTest {

    @Mock
    UniqueContractDataProvider uniqueContractDataProvider;
    @Mock
    private ApiCaller<OrderDbo> orderPlacementApiCaller;

    @InjectMocks
    OrderValidateAndPlacementService orderValidateAndPlacementService;

    @Test
    void testAbsent(){
        ContractDbo contractDBO = ContractDbo.builder().id(1L).build();
        OrderDbo orderData = OrderDbo.builder().id(1L).contractDBO(contractDBO).build();
        when(uniqueContractDataProvider.getExistingContractDataOrCallApi(contractDBO)).thenReturn(Optional.empty());

       orderValidateAndPlacementService.validateAndPlaceOrder(orderData);

        verify(orderPlacementApiCaller, times(0)).callApi(orderData);
    }

    @Test
    void testValid(){
        ContractDbo contractDBO = ContractDbo.builder().right(Types.Right.Call).build();
        OrderDbo orderData = OrderDbo.builder().id(1L).contractDBO(contractDBO).build();
        when(uniqueContractDataProvider.getExistingContractDataOrCallApi(contractDBO)).thenReturn(Optional.of(
                ContractDbo.builder().id(2L).build()));

        orderValidateAndPlacementService.validateAndPlaceOrder(orderData);

        verify(orderPlacementApiCaller, times(1)).callApi(orderData);
        assertEquals(2, orderData.getContractDBO().getId());
        assertNotEquals(Types.Right.Call, orderData.getContractDBO().getRight());

    }
}
