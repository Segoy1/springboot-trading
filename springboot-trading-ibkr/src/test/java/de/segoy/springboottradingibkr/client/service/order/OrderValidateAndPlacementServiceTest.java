package de.segoy.springboottradingibkr.client.service.order;

import com.ib.client.Types;
import de.segoy.springboottradingdata.model.data.entity.ContractDataDBO;
import de.segoy.springboottradingdata.model.data.entity.OrderDataDBO;
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
    private ApiCaller<OrderDataDBO> orderPlacementApiCaller;

    @InjectMocks
    OrderValidateAndPlacementService orderValidateAndPlacementService;

    @Test
    void testAbsent(){
        ContractDataDBO contractDataDBO = ContractDataDBO.builder().id(1L).build();
        OrderDataDBO orderData = OrderDataDBO.builder().id(1L).contractDataDBO(contractDataDBO).build();
        when(uniqueContractDataProvider.getExistingContractDataOrCallApi(contractDataDBO)).thenReturn(Optional.empty());

       orderValidateAndPlacementService.validateAndPlaceOrder(orderData);

        verify(orderPlacementApiCaller, times(0)).callApi(orderData);
    }

    @Test
    void testValid(){
        ContractDataDBO contractDataDBO = ContractDataDBO.builder().right(Types.Right.Call).build();
        OrderDataDBO orderData = OrderDataDBO.builder().id(1L).contractDataDBO(contractDataDBO).build();
        when(uniqueContractDataProvider.getExistingContractDataOrCallApi(contractDataDBO)).thenReturn(Optional.of(
                ContractDataDBO.builder().id(2L).build()));

        orderValidateAndPlacementService.validateAndPlaceOrder(orderData);

        verify(orderPlacementApiCaller, times(1)).callApi(orderData);
        assertEquals(2, orderData.getContractDataDBO().getId());
        assertNotEquals(Types.Right.Call, orderData.getContractDataDBO().getRight());

    }
}
