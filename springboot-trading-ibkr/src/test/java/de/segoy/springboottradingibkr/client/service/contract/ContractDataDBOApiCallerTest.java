package de.segoy.springboottradingibkr.client.service.contract;

import com.ib.client.Contract;
import com.ib.client.EClientSocket;
import de.segoy.springboottradingdata.model.data.entity.ContractDataDBO;
import de.segoy.springboottradingdata.modelconverter.ContractDataToIBKRContract;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContractDataDBOApiCallerTest {

    @Mock
    private EClientSocket client;
    @Mock
    private ContractDataToIBKRContract contractDataToIBKRContract;
    @InjectMocks
    private ContractDataApiCaller contractDataApiCaller;

    @Test
    void testCallApi(){
        ContractDataDBO contractDataDBO = ContractDataDBO.builder().id(1L).build();
        Contract contract = new Contract();
        when(contractDataToIBKRContract.convertContractData(contractDataDBO)).thenReturn(contract);

        contractDataApiCaller.callApi(contractDataDBO);

        verify(client, times(1)).reqContractDetails(1, contract);
    }

}
