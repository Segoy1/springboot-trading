package de.segoy.springboottradingibkr.client.service.contract;

import com.ib.client.Types;
import de.segoy.springboottradingdata.model.data.entity.ComboLegDataDBO;
import de.segoy.springboottradingdata.model.data.entity.ContractDataDBO;
import de.segoy.springboottradingdata.model.subtype.Symbol;
import de.segoy.springboottradingdata.repository.ContractDataRepository;
import de.segoy.springboottradingibkr.client.service.OptionalApiResponseChecker;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContractDataDBOCallAndResponseHandlerTest {


    @Mock
    private ContractDataRepository contractDataRepository;
    @Mock
    private ContractDataApiCaller contractDataApiCaller;
    @Mock
    private OptionalApiResponseChecker<ContractDataDBO> contractDataApiResponseChecker;
    @InjectMocks
    private ContractDataCallAndResponseHandler contractDataCallAndResponseHandler;

    @Test
    void testcallContractDetailsFromAPIWithValidCall() {
        ContractDataDBO contractDataDBO = buildBigContractData();
        contractDataDBO.setSecurityType(Types.SecType.OPT);

        when(contractDataRepository.nextValidId()).thenReturn(9000000);
        when(contractDataApiResponseChecker.checkForApiResponseAndUpdate(9000001)).thenReturn(Optional.of(
                contractDataDBO));

        Optional<ContractDataDBO> testData = contractDataCallAndResponseHandler.callContractDetailsFromAPI(
                contractDataDBO);

        verify(contractDataApiCaller,times(1)).callApi(contractDataDBO);
        assertEquals(123, testData.get().getContractId());
    }
    @Test
    void testcallContractDetailsFromAPIWithInvalidCall() {
        ContractDataDBO contractDataDBO = buildBigContractData();
        contractDataDBO.setSecurityType(Types.SecType.BAG);
        when(contractDataRepository.nextValidId()).thenReturn(2);
        when(contractDataApiResponseChecker.checkForApiResponseAndUpdate(3)).thenReturn(Optional.of(contractDataDBO));
        Optional<ContractDataDBO> testData = contractDataCallAndResponseHandler.callContractDetailsFromAPI(
                contractDataDBO);
        assertEquals(123, testData.get().getContractId());
    }
    @Test
    void testcallContractDetailsFromAPIWithErrorCall() {
        ContractDataDBO contractDataDBO = buildBigContractData();
        contractDataDBO.setSecurityType(Types.SecType.OPT);

        when(contractDataRepository.nextValidId()).thenReturn(2);
        when(contractDataApiResponseChecker.checkForApiResponseAndUpdate(3)).thenReturn(Optional.of(contractDataDBO));
        Optional<ContractDataDBO> testData = contractDataCallAndResponseHandler.callContractDetailsFromAPI(
                contractDataDBO);
        assertEquals(123, testData.get().getContractId());
    }

    private ContractDataDBO buildBigContractData() {
        ComboLegDataDBO comboLegDataDBO = ComboLegDataDBO.builder()
                .contractId(1)
                .ratio(1)
                .action(Types.Action.BUY)
                .exchange("SMART")
                .build();

        return ContractDataDBO.builder()

                .contractId(123)
                .right(Types.Right.Call)
                .symbol(Symbol.SPX)
                .currency("USD")
                .exchange("SMART")
                .lastTradeDate("20231117")
                .multiplier("100")
                .localSymbol("P BMW  20221216 72 M")
                .tradingClass("SPXW")
                .includeExpired(false)
                .comboLegsDescription("description goes here")
                .strike(BigDecimal.valueOf(72))
                .comboLegs(List.of(comboLegDataDBO)).build();
    }

}
