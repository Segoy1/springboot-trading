package de.segoy.springboottradingibkr.client.service.order;

import com.ib.client.Types;
import de.segoy.springboottradingdata.model.ContractData;
import de.segoy.springboottradingdata.model.OrderData;
import de.segoy.springboottradingdata.service.apiresponsecheck.OrderDataApiResponseChecker;
import de.segoy.springboottradingibkr.client.service.contract.UniqueContractDataProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class OrderValidateAndPlacementServiceTest {

    @Mock
    UniqueContractDataProvider uniqueContractDataProvider;
    @Mock
    private OrderPlacementService orderPlacementService;
    @Mock
    private OrderDataApiResponseChecker orderDataApiResponseChecker;
    @InjectMocks
    OrderValidateAndPlacementService orderValidateAndPlacementService;

    @Test
    void testAbsent(){
        ContractData contractData = ContractData.builder().id(1L).build();
        OrderData orderData = OrderData.builder().id(1L).contractData(contractData).build();
        when(uniqueContractDataProvider.getExistingContractDataOrCallApi(contractData)).thenReturn(Optional.empty());

        Optional<OrderData> valid = orderValidateAndPlacementService.validateAndPlaceOrder(orderData);

        verify(orderPlacementService, times(0)).callApi(orderData);
        assertFalse(valid.isPresent());
    }

    @Test
    void testValid(){
        ContractData contractData = ContractData.builder().right(Types.Right.Call).build();
        OrderData orderData = OrderData.builder().id(1L).contractData(contractData).build();
        when(uniqueContractDataProvider.getExistingContractDataOrCallApi(contractData)).thenReturn(Optional.of(ContractData.builder().id(2L).build()));
        when(orderDataApiResponseChecker.checkForApiResponseAndUpdate(orderData.getId().intValue())).thenReturn(Optional.of(orderData));


        Optional<OrderData> valid = orderValidateAndPlacementService.validateAndPlaceOrder(orderData);

        verify(orderPlacementService, times(1)).callApi(orderData);
        assertEquals(2, orderData.getContractData().getId());
        assertNotEquals(Types.Right.Call, orderData.getContractData().getRight());
        assertTrue(valid.isPresent());
    }
}