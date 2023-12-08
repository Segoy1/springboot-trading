package de.segoy.springboottradingibkr.client.service;

import com.ib.client.Types;
import de.segoy.springboottradingdata.model.ContractData;
import de.segoy.springboottradingdata.model.OrderData;
import de.segoy.springboottradingibkr.client.service.contract.ContractDataValidator;
import de.segoy.springboottradingibkr.client.service.contract.UniqueContractDataProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class ContractDataValidatorTest {

    @Mock
    UniqueContractDataProvider uniqueContractDataProvider;
    @InjectMocks
    ContractDataValidator contractDataValidator;

    @Test
    void testAbsent(){
        ContractData contractData = ContractData.builder().id(1).build();
        OrderData orderData = OrderData.builder().contractData(contractData).build();
        when(uniqueContractDataProvider.getExistingContractDataOrCallApi(contractData)).thenReturn(Optional.empty());

        boolean valid = contractDataValidator.validate(orderData);

        assertFalse(valid);
    }

    @Test
    void testValid(){
        ContractData contractData = ContractData.builder().right(Types.Right.Call).build();
        OrderData orderData = OrderData.builder().contractData(contractData).build();
        when(uniqueContractDataProvider.getExistingContractDataOrCallApi(contractData)).thenReturn(Optional.of(ContractData.builder().id(2).build()));

        boolean valid = contractDataValidator.validate(orderData);

        assertEquals(2, orderData.getContractData().getId());
        assertNotEquals(Types.Right.Call, orderData.getContractData().getRight());
        assertTrue(valid);
    }
}