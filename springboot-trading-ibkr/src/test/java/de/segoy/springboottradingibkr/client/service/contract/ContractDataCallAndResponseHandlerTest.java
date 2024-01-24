package de.segoy.springboottradingibkr.client.service.contract;

import com.ib.client.Types;
import de.segoy.springboottradingdata.model.entity.database.ComboLegData;
import de.segoy.springboottradingdata.model.entity.database.ContractData;
import de.segoy.springboottradingdata.repository.ContractDataRepository;
import de.segoy.springboottradingdata.service.apiresponsecheck.OptionalApiResponseChecker;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContractDataCallAndResponseHandlerTest {


    @Mock
    private ContractDataRepository contractDataRepository;
    @Mock
    private ContractDataApiCaller contractDataApiCaller;
    @Mock
    private OptionalApiResponseChecker<ContractData> contractDataApiResponseChecker;
    @InjectMocks
    private ContractDataCallAndResponseHandler contractDataCallAndResponseHandler;

    @Test
    void testcallContractDetailsFromAPIWithValidCall() {
        ContractData contractData = buildBigContractData();
        contractData.setSecurityType(Types.SecType.OPT);

        when(contractDataRepository.nextValidId()).thenReturn(9000000);
        when(contractDataApiResponseChecker.checkForApiResponseAndUpdate(9000001)).thenReturn(Optional.of(contractData));

        Optional<ContractData> testData = contractDataCallAndResponseHandler.callContractDetailsFromAPI(contractData);

        verify(contractDataApiCaller,times(1)).callApi(contractData);
        assertEquals(123, testData.get().getContractId());
    }
    @Test
    void testcallContractDetailsFromAPIWithInvalidCall() {
        ContractData contractData = buildBigContractData();
        contractData.setSecurityType(Types.SecType.BAG);
        when(contractDataRepository.nextValidId()).thenReturn(2);
        when(contractDataApiResponseChecker.checkForApiResponseAndUpdate(3)).thenReturn(Optional.of(contractData));
        Optional<ContractData> testData = contractDataCallAndResponseHandler.callContractDetailsFromAPI(contractData);
        assertEquals(123, testData.get().getContractId());
    }
    @Test
    void testcallContractDetailsFromAPIWithErrorCall() {
        ContractData contractData = buildBigContractData();
        contractData.setSecurityType(Types.SecType.OPT);

        when(contractDataRepository.nextValidId()).thenReturn(2);
        when(contractDataApiResponseChecker.checkForApiResponseAndUpdate(3)).thenReturn(Optional.of(contractData));
        Optional<ContractData> testData = contractDataCallAndResponseHandler.callContractDetailsFromAPI(contractData);
        assertEquals(123, testData.get().getContractId());
    }

    private ContractData buildBigContractData() {
        ComboLegData comboLegData = ComboLegData.builder()
                .contractId(1)
                .ratio(1)
                .action(Types.Action.BUY)
                .exchange("SMART")
                .build();

        return ContractData.builder()

                .contractId(123)
                .right(Types.Right.Call)
                .symbol("SPX")
                .currency("USD")
                .exchange("SMART")
                .lastTradeDate("20231117")
                .multiplier("100")
                .localSymbol("P BMW  20221216 72 M")
                .tradingClass("SPXW")
                .includeExpired(false)
                .comboLegsDescription("description goes here")
                .strike(BigDecimal.valueOf(72))
                .comboLeg(comboLegData).build();
    }

}
