package de.segoy.springboottradingibkr.client.service.order;

import com.ib.client.Types;
import de.segoy.springboottradingdata.model.data.entity.ContractData;
import de.segoy.springboottradingdata.model.data.entity.OrderData;
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
    private ApiCaller<OrderData> orderPlacementApiCaller;

    @InjectMocks
    OrderValidateAndPlacementService orderValidateAndPlacementService;

    @Test
    void testAbsent(){
        ContractData contractData = ContractData.builder().id(1L).build();
        OrderData orderData = OrderData.builder().id(1L).contractData(contractData).build();
        when(uniqueContractDataProvider.getExistingContractDataOrCallApi(contractData)).thenReturn(Optional.empty());

       orderValidateAndPlacementService.validateAndPlaceOrder(orderData);

        verify(orderPlacementApiCaller, times(0)).callApi(orderData);
    }

    @Test
    void testValid(){
        ContractData contractData = ContractData.builder().right(Types.Right.Call).build();
        OrderData orderData = OrderData.builder().id(1L).contractData(contractData).build();
        when(uniqueContractDataProvider.getExistingContractDataOrCallApi(contractData)).thenReturn(Optional.of(ContractData.builder().id(2L).build()));

        orderValidateAndPlacementService.validateAndPlaceOrder(orderData);

        verify(orderPlacementApiCaller, times(1)).callApi(orderData);
        assertEquals(2, orderData.getContractData().getId());
        assertNotEquals(Types.Right.Call, orderData.getContractData().getRight());

    }
}
