package de.segoy.springboottradingibkr.client.service.contract;

import com.ib.client.Contract;
import com.ib.client.EClientSocket;
import de.segoy.springboottradingdata.model.data.entity.ContractDbo;
import de.segoy.springboottradingdata.modelconverter.ContractDboToIBKRContract;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContractDboApiCallerTest {

    @Mock
    private EClientSocket client;
    @Mock
    private ContractDboToIBKRContract contractDboToIBKRContract;
    @InjectMocks
    private ContractDataApiCaller contractDataApiCaller;

    @Test
    void testCallApi(){
        ContractDbo contractDBO = ContractDbo.builder().id(1L).build();
        Contract contract = new Contract();
        when(contractDboToIBKRContract.convertContractData(contractDBO)).thenReturn(contract);

        contractDataApiCaller.callApi(contractDBO);

        verify(client, times(1)).reqContractDetails(1, contract);
    }

}
