package de.segoy.springboottradingibkr.client.service.contract;

import com.ib.client.Contract;
import com.ib.client.EClientSocket;
import de.segoy.springboottradingdata.model.data.entity.ContractData;
import de.segoy.springboottradingdata.modelconverter.ContractDataToIBKRContract;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContractDataApiCallerTest {

    @Mock
    private EClientSocket client;
    @Mock
    private ContractDataToIBKRContract contractDataToIBKRContract;
    @InjectMocks
    private ContractDataApiCaller contractDataApiCaller;

    @Test
    void testCallApi(){
        ContractData contractData = ContractData.builder().id(1L).build();
        Contract contract = new Contract();
        when(contractDataToIBKRContract.convertContractData(contractData)).thenReturn(contract);

        contractDataApiCaller.callApi(contractData);

        verify(client, times(1)).reqContractDetails(1, contract);
    }

}
