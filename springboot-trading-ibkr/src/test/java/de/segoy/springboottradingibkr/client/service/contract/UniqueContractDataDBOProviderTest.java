package de.segoy.springboottradingibkr.client.service.contract;

import com.ib.client.Types;
import de.segoy.springboottradingdata.model.data.entity.ComboLegDataDBO;
import de.segoy.springboottradingdata.model.data.entity.ContractDataDBO;
import de.segoy.springboottradingdata.model.subtype.Symbol;
import de.segoy.springboottradingdata.repository.ContractDataRepository;
import de.segoy.springboottradingdata.service.ComboContractDataFinder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.OptionalLong;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UniqueContractDataDBOProviderTest {

    @Mock
    private ContractDataRepository contractDataRepository;
    @Mock
    private ContractDataCallAndResponseHandler contractDataCallAndResponseHandler;
    @Mock
    private ComboContractDataFinder comboContractDataFinder;
    @InjectMocks
    private UniqueContractDataProvider uniqueContractDataProvider;

    @Test
    void testOPT(){
        ContractDataDBO contractDataDBO =
                ContractDataDBO.builder().lastTradeDate("20240405").symbol(Symbol.SPX).strike(BigDecimal.valueOf(5170)).right(
                        Types.Right.Call).securityType(Types.SecType.OPT).build();
        ContractDataDBO returnData =
                ContractDataDBO.builder().id(1L).lastTradeDate("20240405").symbol(Symbol.SPX).strike(BigDecimal.valueOf(5170)).right(
                Types.Right.Call).securityType(Types.SecType.OPT).build();


        when(contractDataRepository.findFirstByLastTradeDateAndSymbolAndStrikeAndRight(
                contractDataDBO.getLastTradeDate(),
                contractDataDBO.getSymbol(),
                contractDataDBO.getStrike(),
                contractDataDBO.getRight())).thenReturn(Optional.empty());
        when(contractDataCallAndResponseHandler.callContractDetailsFromAPI(
                contractDataDBO)).thenReturn(Optional.of(returnData));

        Optional<ContractDataDBO> result = uniqueContractDataProvider.getExistingContractDataOrCallApi(contractDataDBO);

        assertEquals(1L, result.get().getId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(returnData);

        verify(contractDataRepository, times(1))
                .findFirstByLastTradeDateAndSymbolAndStrikeAndRight(anyString(),any(),
                        any(BigDecimal.class),any(Types.Right.class));
        verify(contractDataCallAndResponseHandler, times(1)).callContractDetailsFromAPI(contractDataDBO);
    }
    @Test
    void testComboWithoutIdButInDB(){
        ComboLegDataDBO cl1 = ComboLegDataDBO.builder().build();
        ComboLegDataDBO cl2 = ComboLegDataDBO.builder().build();
        ContractDataDBO contractDataDBO =
                ContractDataDBO.builder().lastTradeDate("20240405").symbol(Symbol.SPX).strike(BigDecimal.valueOf(5170)).right(
                        Types.Right.Call).securityType(Types.SecType.BAG).comboLegs(List.of(cl1,cl2)).build();
        ContractDataDBO returnData =
                ContractDataDBO.builder().id(1L).lastTradeDate("20240405").symbol(Symbol.SPX).strike(BigDecimal.valueOf(5170)).right(
                        Types.Right.Call).securityType(Types.SecType.BAG).comboLegs(List.of(cl1,cl2)).build();

        when(comboContractDataFinder.checkContractWithComboLegs(contractDataDBO.getComboLegs())).thenReturn(OptionalLong.of(1L));
        when(contractDataRepository.findById(1L)).thenReturn(Optional.of(returnData));

        Optional<ContractDataDBO> result = uniqueContractDataProvider.getExistingContractDataOrCallApi(contractDataDBO);

        assertEquals(1L, result.get().getId());
        verify(comboContractDataFinder, times(1)).checkContractWithComboLegs(contractDataDBO.getComboLegs());
        verify(contractDataRepository, times(1)).findById(1L);
    }
    @Test
    void testComboWithIdNotInDB(){
        ComboLegDataDBO cl1 = ComboLegDataDBO.builder().build();
        ComboLegDataDBO cl2 = ComboLegDataDBO.builder().build();
        ContractDataDBO contractDataDBO =
                ContractDataDBO.builder().id(2L).lastTradeDate("20240405").symbol(Symbol.SPX).strike(BigDecimal.valueOf(5170)).right(
                        Types.Right.Call).securityType(Types.SecType.BAG).comboLegs(List.of(cl1,cl2)).build();
        ContractDataDBO returnData =
                ContractDataDBO.builder().id(1L).lastTradeDate("20240405").symbol(Symbol.SPX).strike(BigDecimal.valueOf(5170)).right(
                        Types.Right.Call).securityType(Types.SecType.BAG).comboLegs(List.of(cl1,cl2)).build();

        when(comboContractDataFinder.checkContractWithComboLegs(contractDataDBO.getComboLegs())).thenReturn(OptionalLong.of(1L));
        when(contractDataRepository.findById(2L)).thenReturn(Optional.empty());
        when(contractDataRepository.findById(1L)).thenReturn(Optional.of(returnData));

        Optional<ContractDataDBO> result = uniqueContractDataProvider.getExistingContractDataOrCallApi(contractDataDBO);

        assertEquals(1L, result.get().getId());
        verify(comboContractDataFinder, times(1)).checkContractWithComboLegs(contractDataDBO.getComboLegs());
        verify(contractDataRepository, times(1)).findById(1L);
    }
    @Test
    void testComboWithIdInDB(){
        ComboLegDataDBO cl1 = ComboLegDataDBO.builder().build();
        ComboLegDataDBO cl2 = ComboLegDataDBO.builder().build();
        ContractDataDBO contractDataDBO =
                ContractDataDBO.builder().id(2L).lastTradeDate("20240405").symbol(Symbol.SPX).strike(BigDecimal.valueOf(5170)).right(
                        Types.Right.Call).securityType(Types.SecType.BAG).comboLegs(List.of(cl1,cl2)).build();

        when(contractDataRepository.findById(2L)).thenReturn(Optional.of(contractDataDBO));

        Optional<ContractDataDBO> result = uniqueContractDataProvider.getExistingContractDataOrCallApi(contractDataDBO);

        assertEquals(2L, result.get().getId());
        verify(contractDataRepository, times(2)).findById(2L);
    }
    @Test
    void testComboWithoutIdNotInDB(){
        ComboLegDataDBO cl1 = ComboLegDataDBO.builder().build();
        ComboLegDataDBO cl2 = ComboLegDataDBO.builder().build();
        ContractDataDBO contractDataDBO =
                ContractDataDBO.builder().lastTradeDate("20240405").symbol(Symbol.SPX).strike(BigDecimal.valueOf(5170)).right(
                        Types.Right.Call).securityType(Types.SecType.BAG).comboLegs(List.of(cl1,cl2)).build();
        ContractDataDBO returnData =
                ContractDataDBO.builder().id(1L).lastTradeDate("20240405").symbol(Symbol.SPX).strike(BigDecimal.valueOf(5170)).right(
                        Types.Right.Call).securityType(Types.SecType.BAG).comboLegs(List.of(cl1,cl2)).build();

        when(comboContractDataFinder.checkContractWithComboLegs(contractDataDBO.getComboLegs())).thenReturn(OptionalLong.empty());
        when(contractDataRepository.save(contractDataDBO)).thenReturn(contractDataDBO);

        Optional<ContractDataDBO> result = uniqueContractDataProvider.getExistingContractDataOrCallApi(contractDataDBO);

        assertEquals(true, result.isPresent());
        verify(comboContractDataFinder, times(1)).checkContractWithComboLegs(contractDataDBO.getComboLegs());
        verify(contractDataRepository,times(1)).save(contractDataDBO);
    }
    @Test
    void testIndex(){
        ContractDataDBO contractDataDBO =
                ContractDataDBO.builder().symbol(Symbol.SPX).securityType(Types.SecType.IND).currency("USD").build();
        ContractDataDBO returnData =
                ContractDataDBO.builder().id(1L).symbol(Symbol.SPX).securityType(Types.SecType.IND).currency("USD").build();
        indexOrStock(contractDataDBO, returnData);

    } @Test
    void testIndexPresent(){
        ContractDataDBO contractDataDBO =
                ContractDataDBO.builder().symbol(Symbol.SPX).securityType(Types.SecType.IND).currency("USD").build();
        ContractDataDBO returnData =
                ContractDataDBO.builder().id(1L).symbol(Symbol.SPX).securityType(Types.SecType.IND).currency("USD").build();
        indexOrStockPresent(contractDataDBO);
    }
    @Test
    void testStock(){
        ContractDataDBO contractDataDBO =
                ContractDataDBO.builder().symbol(Symbol.SPX).securityType(Types.SecType.STK).currency("USD").build();
        ContractDataDBO returnData =
                ContractDataDBO.builder().id(1L).symbol(Symbol.SPX).securityType(Types.SecType.STK).currency("USD").build();

        indexOrStock(contractDataDBO, returnData);
    }
    @Test
    void testStockPresent(){
        ContractDataDBO contractDataDBO =
                ContractDataDBO.builder().symbol(Symbol.SPX).securityType(Types.SecType.STK).currency("USD").build();
        ContractDataDBO returnData =
                ContractDataDBO.builder().id(1L).symbol(Symbol.SPX).securityType(Types.SecType.STK).currency("USD").build();
        indexOrStockPresent(contractDataDBO);
    }

    private void indexOrStock(ContractDataDBO contractDataDBO, ContractDataDBO returnData) {
        when(contractDataRepository.findFirstBySymbolAndSecurityTypeAndCurrency(contractDataDBO.getSymbol(),
                contractDataDBO.getSecurityType(),
                contractDataDBO.getCurrency())).thenReturn(Optional.empty());
        when(contractDataCallAndResponseHandler.callContractDetailsFromAPI(
                contractDataDBO)).thenReturn(Optional.of(returnData));

        Optional<ContractDataDBO> result = uniqueContractDataProvider.getExistingContractDataOrCallApi(contractDataDBO);

        assertEquals(1L, result.get().getId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(returnData);

        verify(contractDataRepository, times(1))
                .findFirstBySymbolAndSecurityTypeAndCurrency(any(),
                        any(Types.SecType.class),anyString());
        verify(contractDataCallAndResponseHandler, times(1)).callContractDetailsFromAPI(contractDataDBO);
    }
    private void indexOrStockPresent(ContractDataDBO contractDataDBO) {
        when(contractDataRepository.findFirstBySymbolAndSecurityTypeAndCurrency(contractDataDBO.getSymbol(),
                contractDataDBO.getSecurityType(),
                contractDataDBO.getCurrency())).thenReturn(Optional.of(contractDataDBO));
        Optional<ContractDataDBO> result = uniqueContractDataProvider.getExistingContractDataOrCallApi(contractDataDBO);

        assertEquals(null, result.get().getId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(contractDataDBO);

        verify(contractDataRepository, times(1))
                .findFirstBySymbolAndSecurityTypeAndCurrency(any(),
                        any(Types.SecType.class),anyString());
        verify(contractDataCallAndResponseHandler, never()).callContractDetailsFromAPI(any(ContractDataDBO.class));
    }
    @Test
    void testNoValidType(){
        List<Types.SecType> validTypes = List.of(Types.SecType.BAG, Types.SecType.OPT, Types.SecType.STK,
                Types.SecType.IND);
        for(Types.SecType secType: Types.SecType.values()){
        ContractDataDBO contractDataDBO = ContractDataDBO.builder().securityType(secType).build();
        if(!validTypes.contains(secType)){
            assertThat(uniqueContractDataProvider.getExistingContractDataOrCallApi(contractDataDBO)).isEmpty();
        }

        }
    }
}
