package de.segoy.springboottradingibkr.client.service.contract;

import com.ib.client.Types;
import de.segoy.springboottradingdata.model.data.entity.ComboLegDbo;
import de.segoy.springboottradingdata.model.data.entity.ContractDbo;
import de.segoy.springboottradingdata.model.subtype.Symbol;
import de.segoy.springboottradingdata.repository.ContractRepository;
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
class UniqueContractDboProviderTest {

    @Mock
    private ContractRepository contractRepository;
    @Mock
    private ContractDataCallAndResponseHandler contractDataCallAndResponseHandler;
    @Mock
    private ComboContractDataFinder comboContractDataFinder;
    @InjectMocks
    private UniqueContractDataProvider uniqueContractDataProvider;

    @Test
    void testOPT(){
        ContractDbo contractDBO =
                ContractDbo.builder().lastTradeDate("20240405").symbol(Symbol.SPX).strike(BigDecimal.valueOf(5170)).right(
                        Types.Right.Call).securityType(Types.SecType.OPT).build();
        ContractDbo returnData =
                ContractDbo.builder().id(1L).lastTradeDate("20240405").symbol(Symbol.SPX).strike(BigDecimal.valueOf(5170)).right(
                Types.Right.Call).securityType(Types.SecType.OPT).build();


        when(contractRepository.findFirstByLastTradeDateAndSymbolAndStrikeAndRight(
                contractDBO.getLastTradeDate(),
                contractDBO.getSymbol(),
                contractDBO.getStrike(),
                contractDBO.getRight())).thenReturn(Optional.empty());
        when(contractDataCallAndResponseHandler.callContractDetailsFromAPI(
                contractDBO)).thenReturn(Optional.of(returnData));

        Optional<ContractDbo> result = uniqueContractDataProvider.getExistingContractDataOrCallApi(contractDBO);

        assertEquals(1L, result.get().getId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(returnData);

        verify(contractRepository, times(1))
                .findFirstByLastTradeDateAndSymbolAndStrikeAndRight(anyString(),any(),
                        any(BigDecimal.class),any(Types.Right.class));
        verify(contractDataCallAndResponseHandler, times(1)).callContractDetailsFromAPI(contractDBO);
    }
    @Test
    void testComboWithoutIdButInDB(){
        ComboLegDbo cl1 = ComboLegDbo.builder().build();
        ComboLegDbo cl2 = ComboLegDbo.builder().build();
        ContractDbo contractDBO =
                ContractDbo.builder().lastTradeDate("20240405").symbol(Symbol.SPX).strike(BigDecimal.valueOf(5170)).right(
                        Types.Right.Call).securityType(Types.SecType.BAG).comboLegs(List.of(cl1,cl2)).build();
        ContractDbo returnData =
                ContractDbo.builder().id(1L).lastTradeDate("20240405").symbol(Symbol.SPX).strike(BigDecimal.valueOf(5170)).right(
                        Types.Right.Call).securityType(Types.SecType.BAG).comboLegs(List.of(cl1,cl2)).build();

        when(comboContractDataFinder.findIdOfContractWithComboLegs(contractDBO.getComboLegs())).thenReturn(OptionalLong.of(1L));
        when(contractRepository.findById(1L)).thenReturn(Optional.of(returnData));

        Optional<ContractDbo> result = uniqueContractDataProvider.getExistingContractDataOrCallApi(contractDBO);

        assertEquals(1L, result.get().getId());
        verify(comboContractDataFinder, times(1)).findIdOfContractWithComboLegs(contractDBO.getComboLegs());
        verify(contractRepository, times(1)).findById(1L);
    }
    @Test
    void testComboWithIdNotInDB(){
        ComboLegDbo cl1 = ComboLegDbo.builder().build();
        ComboLegDbo cl2 = ComboLegDbo.builder().build();
        ContractDbo contractDBO =
                ContractDbo.builder().id(2L).lastTradeDate("20240405").symbol(Symbol.SPX).strike(BigDecimal.valueOf(5170)).right(
                        Types.Right.Call).securityType(Types.SecType.BAG).comboLegs(List.of(cl1,cl2)).build();
        ContractDbo returnData =
                ContractDbo.builder().id(1L).lastTradeDate("20240405").symbol(Symbol.SPX).strike(BigDecimal.valueOf(5170)).right(
                        Types.Right.Call).securityType(Types.SecType.BAG).comboLegs(List.of(cl1,cl2)).build();

        when(comboContractDataFinder.findIdOfContractWithComboLegs(contractDBO.getComboLegs())).thenReturn(OptionalLong.of(1L));
        when(contractRepository.findById(2L)).thenReturn(Optional.empty());
        when(contractRepository.findById(1L)).thenReturn(Optional.of(returnData));

        Optional<ContractDbo> result = uniqueContractDataProvider.getExistingContractDataOrCallApi(contractDBO);

        assertEquals(1L, result.get().getId());
        verify(comboContractDataFinder, times(1)).findIdOfContractWithComboLegs(contractDBO.getComboLegs());
        verify(contractRepository, times(1)).findById(1L);
    }
    @Test
    void testComboWithIdInDB(){
        ComboLegDbo cl1 = ComboLegDbo.builder().build();
        ComboLegDbo cl2 = ComboLegDbo.builder().build();
        ContractDbo contractDBO =
                ContractDbo.builder().id(2L).lastTradeDate("20240405").symbol(Symbol.SPX).strike(BigDecimal.valueOf(5170)).right(
                        Types.Right.Call).securityType(Types.SecType.BAG).comboLegs(List.of(cl1,cl2)).build();

        when(contractRepository.findById(2L)).thenReturn(Optional.of(contractDBO));

        Optional<ContractDbo> result = uniqueContractDataProvider.getExistingContractDataOrCallApi(contractDBO);

        assertEquals(2L, result.get().getId());
        verify(contractRepository, times(2)).findById(2L);
    }
    @Test
    void testComboWithoutIdNotInDB(){
        ComboLegDbo cl1 = ComboLegDbo.builder().build();
        ComboLegDbo cl2 = ComboLegDbo.builder().build();
        ContractDbo contractDBO =
                ContractDbo.builder().lastTradeDate("20240405").symbol(Symbol.SPX).strike(BigDecimal.valueOf(5170)).right(
                        Types.Right.Call).securityType(Types.SecType.BAG).comboLegs(List.of(cl1,cl2)).build();
        ContractDbo returnData =
                ContractDbo.builder().id(1L).lastTradeDate("20240405").symbol(Symbol.SPX).strike(BigDecimal.valueOf(5170)).right(
                        Types.Right.Call).securityType(Types.SecType.BAG).comboLegs(List.of(cl1,cl2)).build();

        when(comboContractDataFinder.findIdOfContractWithComboLegs(contractDBO.getComboLegs())).thenReturn(OptionalLong.empty());
        when(contractRepository.save(contractDBO)).thenReturn(contractDBO);

        Optional<ContractDbo> result = uniqueContractDataProvider.getExistingContractDataOrCallApi(contractDBO);

        assertEquals(true, result.isPresent());
        verify(comboContractDataFinder, times(1)).findIdOfContractWithComboLegs(contractDBO.getComboLegs());
        verify(contractRepository,times(1)).save(contractDBO);
    }
    @Test
    void testIndex(){
        ContractDbo contractDBO =
                ContractDbo.builder().symbol(Symbol.SPX).securityType(Types.SecType.IND).currency("USD").build();
        ContractDbo returnData =
                ContractDbo.builder().id(1L).symbol(Symbol.SPX).securityType(Types.SecType.IND).currency("USD").build();
        indexOrStock(contractDBO, returnData);

    } @Test
    void testIndexPresent(){
        ContractDbo contractDBO =
                ContractDbo.builder().symbol(Symbol.SPX).securityType(Types.SecType.IND).currency("USD").build();
        ContractDbo returnData =
                ContractDbo.builder().id(1L).symbol(Symbol.SPX).securityType(Types.SecType.IND).currency("USD").build();
        indexOrStockPresent(contractDBO);
    }
    @Test
    void testStock(){
        ContractDbo contractDBO =
                ContractDbo.builder().symbol(Symbol.SPX).securityType(Types.SecType.STK).currency("USD").build();
        ContractDbo returnData =
                ContractDbo.builder().id(1L).symbol(Symbol.SPX).securityType(Types.SecType.STK).currency("USD").build();

        indexOrStock(contractDBO, returnData);
    }
    @Test
    void testStockPresent(){
        ContractDbo contractDBO =
                ContractDbo.builder().symbol(Symbol.SPX).securityType(Types.SecType.STK).currency("USD").build();
        ContractDbo returnData =
                ContractDbo.builder().id(1L).symbol(Symbol.SPX).securityType(Types.SecType.STK).currency("USD").build();
        indexOrStockPresent(contractDBO);
    }

    private void indexOrStock(ContractDbo contractDBO, ContractDbo returnData) {
        when(contractRepository.findFirstBySymbolAndSecurityTypeAndCurrency(contractDBO.getSymbol(),
                contractDBO.getSecurityType(),
                contractDBO.getCurrency())).thenReturn(Optional.empty());
        when(contractDataCallAndResponseHandler.callContractDetailsFromAPI(
                contractDBO)).thenReturn(Optional.of(returnData));

        Optional<ContractDbo> result = uniqueContractDataProvider.getExistingContractDataOrCallApi(contractDBO);

        assertEquals(1L, result.get().getId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(returnData);

        verify(contractRepository, times(1))
                .findFirstBySymbolAndSecurityTypeAndCurrency(any(),
                        any(Types.SecType.class),anyString());
        verify(contractDataCallAndResponseHandler, times(1)).callContractDetailsFromAPI(contractDBO);
    }
    private void indexOrStockPresent(ContractDbo contractDBO) {
        when(contractRepository.findFirstBySymbolAndSecurityTypeAndCurrency(contractDBO.getSymbol(),
                contractDBO.getSecurityType(),
                contractDBO.getCurrency())).thenReturn(Optional.of(contractDBO));
        Optional<ContractDbo> result = uniqueContractDataProvider.getExistingContractDataOrCallApi(contractDBO);

        assertEquals(null, result.get().getId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(contractDBO);

        verify(contractRepository, times(1))
                .findFirstBySymbolAndSecurityTypeAndCurrency(any(),
                        any(Types.SecType.class),anyString());
        verify(contractDataCallAndResponseHandler, never()).callContractDetailsFromAPI(any(ContractDbo.class));
    }
    @Test
    void testNoValidType(){
        List<Types.SecType> validTypes = List.of(Types.SecType.BAG, Types.SecType.OPT, Types.SecType.STK,
                Types.SecType.IND);
        for(Types.SecType secType: Types.SecType.values()){
        ContractDbo contractDBO = ContractDbo.builder().securityType(secType).build();
        if(!validTypes.contains(secType)){
            assertThat(uniqueContractDataProvider.getExistingContractDataOrCallApi(contractDBO)).isEmpty();
        }

        }
    }
}
