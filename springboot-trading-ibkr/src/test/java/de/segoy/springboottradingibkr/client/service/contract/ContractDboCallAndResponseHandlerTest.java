package de.segoy.springboottradingibkr.client.service.contract;

import com.ib.client.Types;
import de.segoy.springboottradingdata.model.data.entity.ComboLegDbo;
import de.segoy.springboottradingdata.model.data.entity.ContractDbo;
import de.segoy.springboottradingdata.model.subtype.Symbol;
import de.segoy.springboottradingdata.repository.ContractRepository;
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
class ContractDboCallAndResponseHandlerTest {


    @Mock
    private ContractRepository contractRepository;
    @Mock
    private ContractDataApiCaller contractDataApiCaller;
    @Mock
    private OptionalApiResponseChecker<ContractDbo> contractDataApiResponseChecker;
    @InjectMocks
    private ContractDataCallAndResponseHandler contractDataCallAndResponseHandler;

    @Test
    void testcallContractDetailsFromAPIWithValidCall() {
        ContractDbo contractDBO = buildBigContractData();
        contractDBO.setSecurityType(Types.SecType.OPT);

        when(contractRepository.nextValidId()).thenReturn(9000000);
        when(contractDataApiResponseChecker.checkForApiResponseAndUpdate(9000001)).thenReturn(Optional.of(
                contractDBO));

        Optional<ContractDbo> testData = contractDataCallAndResponseHandler.callContractDetailsFromAPI(
                contractDBO);

        verify(contractDataApiCaller,times(1)).callApi(contractDBO);
        assertEquals(123, testData.get().getContractId());
    }
    @Test
    void testcallContractDetailsFromAPIWithInvalidCall() {
        ContractDbo contractDBO = buildBigContractData();
        contractDBO.setSecurityType(Types.SecType.BAG);
        when(contractRepository.nextValidId()).thenReturn(2);
        when(contractDataApiResponseChecker.checkForApiResponseAndUpdate(3)).thenReturn(Optional.of(contractDBO));
        Optional<ContractDbo> testData = contractDataCallAndResponseHandler.callContractDetailsFromAPI(
                contractDBO);
        assertEquals(123, testData.get().getContractId());
    }
    @Test
    void testcallContractDetailsFromAPIWithErrorCall() {
        ContractDbo contractDBO = buildBigContractData();
        contractDBO.setSecurityType(Types.SecType.OPT);

        when(contractRepository.nextValidId()).thenReturn(2);
        when(contractDataApiResponseChecker.checkForApiResponseAndUpdate(3)).thenReturn(Optional.of(contractDBO));
        Optional<ContractDbo> testData = contractDataCallAndResponseHandler.callContractDetailsFromAPI(
                contractDBO);
        assertEquals(123, testData.get().getContractId());
    }

    private ContractDbo buildBigContractData() {
        ComboLegDbo comboLegDBO = ComboLegDbo.builder()
                .contractId(1)
                .ratio(1)
                .action(Types.Action.BUY)
                .exchange("SMART")
                .build();

        return ContractDbo.builder()

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
                .comboLegs(List.of(comboLegDBO)).build();
    }

}
