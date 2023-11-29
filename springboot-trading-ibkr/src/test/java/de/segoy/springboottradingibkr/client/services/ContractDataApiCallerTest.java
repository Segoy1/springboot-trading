package de.segoy.springboottradingibkr.client.services;

import com.ib.client.EClientSocket;
import com.ib.client.Types;
import de.segoy.springboottradingdata.model.ComboLegData;
import de.segoy.springboottradingdata.model.ContractData;
import de.segoy.springboottradingdata.modelconverter.ContractDataToIBKRContract;
import de.segoy.springboottradingdata.repository.ContractDataRepository;
import de.segoy.springboottradingdata.repository.message.ErrorMessageRepository;
import de.segoy.springboottradingdata.service.RepositoryRefreshService;
import de.segoy.springboottradingdata.type.Currency;
import de.segoy.springboottradingdata.type.Symbol;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ContractDataApiCallerTest {

    @Mock
    private ContractDataToIBKRContract contractDataToIBKRContract;
    @Mock
    private EClientSocket client;
    @Mock
    private ContractDataRepository contractDataRepository;
    @Mock
    private RepositoryRefreshService repositoryRefreshService;
    @Mock
    private ErrorMessageRepository errorMessageRepository;
    @InjectMocks
    private ContractDataApiCaller contractDataApiCaller;

    @Test
    void testcallContractDetailsFromAPIWithValidCall() {
        ContractData contractData = buildBigContractData();
        contractData.setSecurityType(Types.SecType.OPT);
        contractData.setTouchedByApi(true);

        when(contractDataRepository.findById(9000000)).thenReturn(Optional.of(contractData));

        ContractData testData = contractDataApiCaller.callContractDetailsFromAPI(contractData);

        assertEquals(123, testData.getContractId());
    }
    @Test
    void testcallContractDetailsFromAPIWithInvalidCall() {
        ContractData contractData = buildBigContractData();
        contractData.setSecurityType(Types.SecType.BAG);
        when(contractDataRepository.findById(9000000)).thenReturn(Optional.of(contractData));
        ContractData testData = contractDataApiCaller.callContractDetailsFromAPI(contractData);
        assertEquals(123, testData.getContractId());
    }
    @Test
    void testcallContractDetailsFromAPIWithErrorCall() {
        ContractData contractData = buildBigContractData();
        contractData.setSecurityType(Types.SecType.OPT);

        when(contractDataRepository.findById(9000000)).thenReturn(Optional.of(contractData));
        when(errorMessageRepository.existsById(9000000)).thenReturn(true);
        ContractData testData = contractDataApiCaller.callContractDetailsFromAPI(contractData);
        assertEquals(123, testData.getContractId());
    }

    private ContractData buildBigContractData() {
        ComboLegData comboLegData = ComboLegData.builder()
                .contractId(1)
                .ratio(1)
                .action(Types.Action.BUY)
                .exchange("SMART")
                .build();

        return ContractData.builder()
                .id(9000000)
                .contractId(123)
                .right(Types.Right.Call)
                .symbol(Symbol.SPX.toString())
                .currency(Currency.USD.toString())
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