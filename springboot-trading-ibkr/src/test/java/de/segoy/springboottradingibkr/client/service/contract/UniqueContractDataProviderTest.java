package de.segoy.springboottradingibkr.client.service.contract;

import com.ib.client.Types;
import de.segoy.springboottradingdata.model.data.entity.ComboLegData;
import de.segoy.springboottradingdata.model.data.entity.ContractData;
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
class UniqueContractDataProviderTest {

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
        ContractData contractData =
                ContractData.builder().lastTradeDate("20240405").symbol("SPX").strike(BigDecimal.valueOf(5170)).right(
                        Types.Right.Call).securityType(Types.SecType.OPT).build();
        ContractData returnData =
                ContractData.builder().id(1L).lastTradeDate("20240405").symbol("SPX").strike(BigDecimal.valueOf(5170)).right(
                Types.Right.Call).securityType(Types.SecType.OPT).build();


        when(contractDataRepository.findFirstByLastTradeDateAndSymbolAndStrikeAndRight(
                contractData.getLastTradeDate(),
                contractData.getSymbol(),
                contractData.getStrike(),
                contractData.getRight())).thenReturn(Optional.empty());
        when(contractDataCallAndResponseHandler.callContractDetailsFromAPI(
                contractData)).thenReturn(Optional.of(returnData));

        Optional<ContractData> result = uniqueContractDataProvider.getExistingContractDataOrCallApi(contractData);

        assertEquals(1L, result.get().getId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(returnData);

        verify(contractDataRepository, times(1))
                .findFirstByLastTradeDateAndSymbolAndStrikeAndRight(anyString(),anyString(),
                        any(BigDecimal.class),any(Types.Right.class));
        verify(contractDataCallAndResponseHandler, times(1)).callContractDetailsFromAPI(contractData);
    }
    @Test
    void testComboWithoutIdButInDB(){
        ComboLegData cl1 = ComboLegData.builder().build();
        ComboLegData cl2 = ComboLegData.builder().build();
        ContractData contractData =
                ContractData.builder().lastTradeDate("20240405").symbol("SPX").strike(BigDecimal.valueOf(5170)).right(
                        Types.Right.Call).securityType(Types.SecType.BAG).comboLegs(List.of(cl1,cl2)).build();
        ContractData returnData =
                ContractData.builder().id(1L).lastTradeDate("20240405").symbol("SPX").strike(BigDecimal.valueOf(5170)).right(
                        Types.Right.Call).securityType(Types.SecType.BAG).comboLegs(List.of(cl1,cl2)).build();

        when(comboContractDataFinder.checkContractWithComboLegs(contractData.getComboLegs())).thenReturn(OptionalLong.of(1L));
        when(contractDataRepository.findById(1L)).thenReturn(Optional.of(returnData));

        Optional<ContractData> result = uniqueContractDataProvider.getExistingContractDataOrCallApi(contractData);

        assertEquals(1L, result.get().getId());
        verify(comboContractDataFinder, times(1)).checkContractWithComboLegs(contractData.getComboLegs());
        verify(contractDataRepository, times(1)).findById(1L);
    }
    @Test
    void testComboWithIdNotInDB(){
        ComboLegData cl1 = ComboLegData.builder().build();
        ComboLegData cl2 = ComboLegData.builder().build();
        ContractData contractData =
                ContractData.builder().id(2L).lastTradeDate("20240405").symbol("SPX").strike(BigDecimal.valueOf(5170)).right(
                        Types.Right.Call).securityType(Types.SecType.BAG).comboLegs(List.of(cl1,cl2)).build();
        ContractData returnData =
                ContractData.builder().id(1L).lastTradeDate("20240405").symbol("SPX").strike(BigDecimal.valueOf(5170)).right(
                        Types.Right.Call).securityType(Types.SecType.BAG).comboLegs(List.of(cl1,cl2)).build();

        when(comboContractDataFinder.checkContractWithComboLegs(contractData.getComboLegs())).thenReturn(OptionalLong.of(1L));
        when(contractDataRepository.findById(2L)).thenReturn(Optional.empty());
        when(contractDataRepository.findById(1L)).thenReturn(Optional.of(returnData));

        Optional<ContractData> result = uniqueContractDataProvider.getExistingContractDataOrCallApi(contractData);

        assertEquals(1L, result.get().getId());
        verify(comboContractDataFinder, times(1)).checkContractWithComboLegs(contractData.getComboLegs());
        verify(contractDataRepository, times(1)).findById(1L);
    }
    @Test
    void testComboWithIdInDB(){
        ComboLegData cl1 = ComboLegData.builder().build();
        ComboLegData cl2 = ComboLegData.builder().build();
        ContractData contractData =
                ContractData.builder().id(2L).lastTradeDate("20240405").symbol("SPX").strike(BigDecimal.valueOf(5170)).right(
                        Types.Right.Call).securityType(Types.SecType.BAG).comboLegs(List.of(cl1,cl2)).build();

        when(contractDataRepository.findById(2L)).thenReturn(Optional.of(contractData));

        Optional<ContractData> result = uniqueContractDataProvider.getExistingContractDataOrCallApi(contractData);

        assertEquals(2L, result.get().getId());
        verify(contractDataRepository, times(2)).findById(2L);
    }
    @Test
    void testComboWithoutIdNotInDB(){
        ComboLegData cl1 = ComboLegData.builder().build();
        ComboLegData cl2 = ComboLegData.builder().build();
        ContractData contractData =
                ContractData.builder().lastTradeDate("20240405").symbol("SPX").strike(BigDecimal.valueOf(5170)).right(
                        Types.Right.Call).securityType(Types.SecType.BAG).comboLegs(List.of(cl1,cl2)).build();
        ContractData returnData =
                ContractData.builder().id(1L).lastTradeDate("20240405").symbol("SPX").strike(BigDecimal.valueOf(5170)).right(
                        Types.Right.Call).securityType(Types.SecType.BAG).comboLegs(List.of(cl1,cl2)).build();

        when(comboContractDataFinder.checkContractWithComboLegs(contractData.getComboLegs())).thenReturn(OptionalLong.empty());
        when(contractDataRepository.save(contractData)).thenReturn(contractData);

        Optional<ContractData> result = uniqueContractDataProvider.getExistingContractDataOrCallApi(contractData);

        assertEquals(true, result.isPresent());
        verify(comboContractDataFinder, times(1)).checkContractWithComboLegs(contractData.getComboLegs());
        verify(contractDataRepository,times(1)).save(contractData);
    }
    @Test
    void testIndex(){
        ContractData contractData =
                ContractData.builder().symbol("SPX").securityType(Types.SecType.IND).currency("USD").build();
        ContractData returnData =
                ContractData.builder().id(1L).symbol("SPX").securityType(Types.SecType.IND).currency("USD").build();
        indexOrStock(contractData, returnData);

    } @Test
    void testIndexPresent(){
        ContractData contractData =
                ContractData.builder().symbol("SPX").securityType(Types.SecType.IND).currency("USD").build();
        ContractData returnData =
                ContractData.builder().id(1L).symbol("SPX").securityType(Types.SecType.IND).currency("USD").build();
        indexOrStockPresent(contractData);
    }
    @Test
    void testStock(){
        ContractData contractData =
                ContractData.builder().symbol("SPX").securityType(Types.SecType.STK).currency("USD").build();
        ContractData returnData =
                ContractData.builder().id(1L).symbol("SPX").securityType(Types.SecType.STK).currency("USD").build();

        indexOrStock(contractData, returnData);
    }
    @Test
    void testStockPresent(){
        ContractData contractData =
                ContractData.builder().symbol("SPX").securityType(Types.SecType.STK).currency("USD").build();
        ContractData returnData =
                ContractData.builder().id(1L).symbol("SPX").securityType(Types.SecType.STK).currency("USD").build();
        indexOrStockPresent(contractData);
    }

    private void indexOrStock(ContractData contractData, ContractData returnData) {
        when(contractDataRepository.findFirstBySymbolAndSecurityTypeAndCurrency(contractData.getSymbol(),
                contractData.getSecurityType(),
                contractData.getCurrency())).thenReturn(Optional.empty());
        when(contractDataCallAndResponseHandler.callContractDetailsFromAPI(
                contractData)).thenReturn(Optional.of(returnData));

        Optional<ContractData> result = uniqueContractDataProvider.getExistingContractDataOrCallApi(contractData);

        assertEquals(1L, result.get().getId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(returnData);

        verify(contractDataRepository, times(1))
                .findFirstBySymbolAndSecurityTypeAndCurrency(anyString(),
                        any(Types.SecType.class),anyString());
        verify(contractDataCallAndResponseHandler, times(1)).callContractDetailsFromAPI(contractData);
    }
    private void indexOrStockPresent(ContractData contractData) {
        when(contractDataRepository.findFirstBySymbolAndSecurityTypeAndCurrency(contractData.getSymbol(),
                contractData.getSecurityType(),
                contractData.getCurrency())).thenReturn(Optional.of(contractData));
        Optional<ContractData> result = uniqueContractDataProvider.getExistingContractDataOrCallApi(contractData);

        assertEquals(null, result.get().getId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(contractData);

        verify(contractDataRepository, times(1))
                .findFirstBySymbolAndSecurityTypeAndCurrency(anyString(),
                        any(Types.SecType.class),anyString());
        verify(contractDataCallAndResponseHandler, never()).callContractDetailsFromAPI(any(ContractData.class));
    }
    @Test
    void testNoValidType(){
        List<Types.SecType> validTypes = List.of(Types.SecType.BAG, Types.SecType.OPT, Types.SecType.STK,
                Types.SecType.IND);
        for(Types.SecType secType: Types.SecType.values()){
        ContractData contractData = ContractData.builder().securityType(secType).build();
        if(!validTypes.contains(secType)){
            assertThat(uniqueContractDataProvider.getExistingContractDataOrCallApi(contractData)).isEmpty();
        }

        }
    }
}
