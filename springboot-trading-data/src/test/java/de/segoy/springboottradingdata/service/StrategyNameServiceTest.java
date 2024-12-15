package de.segoy.springboottradingdata.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.ib.client.Types;
import de.segoy.springboottradingdata.model.data.entity.ComboLegDbo;
import de.segoy.springboottradingdata.model.data.entity.ContractDbo;
import de.segoy.springboottradingdata.model.subtype.Strategy;
import de.segoy.springboottradingdata.repository.ContractRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StrategyNameServiceTest {

  @Mock private ContractRepository contractRepository;
  @InjectMocks private StrategyNameService strategyNameService;

  @Test
  void getStrategyNameFromComboLegsService_WithIronButterfly() {
    ContractDbo contract1 =
        ContractDbo.builder()
            .right(Types.Right.Call)
            .strike(BigDecimal.valueOf(15))
            .contractId(1)
            .build();
    ContractDbo contract2 =
        ContractDbo.builder()
            .right(Types.Right.Call)
            .strike(BigDecimal.valueOf(10))
            .contractId(2)
            .build();
    ContractDbo contract3 =
        ContractDbo.builder()
            .right(Types.Right.Put)
            .strike(BigDecimal.valueOf(10))
            .contractId(3)
            .build();
    ContractDbo contract4 =
        ContractDbo.builder()
            .right(Types.Right.Put)
            .strike(BigDecimal.valueOf(5))
            .contractId(4)
            .build();
    when(contractRepository.findFirstByContractId(1)).thenReturn(Optional.of(contract1));
    when(contractRepository.findFirstByContractId(2)).thenReturn(Optional.of(contract2));
    when(contractRepository.findFirstByContractId(3)).thenReturn(Optional.of(contract3));
    when(contractRepository.findFirstByContractId(4)).thenReturn(Optional.of(contract4));

    ComboLegDbo leg = ComboLegDbo.builder().contractId(1).ratio(1).action(Types.Action.BUY).build();
    ComboLegDbo leg2 =
        ComboLegDbo.builder().contractId(2).ratio(1).action(Types.Action.SELL).build();
    ComboLegDbo leg3 =
        ComboLegDbo.builder().contractId(3).ratio(1).action(Types.Action.SELL).build();
    ComboLegDbo leg4 =
        ComboLegDbo.builder().contractId(4).ratio(1).action(Types.Action.BUY).build();

    assertThat(strategyNameService.resolveStrategyFromComboLegs(List.of(leg, leg2, leg3, leg4)))
        .isEqualTo(Strategy.IRON_BUTTERFLY);
  }
  @Test
  void getStrategyNameFromComboLegsService_WithShortIronButterfly() {
    ContractDbo contract1 =
            ContractDbo.builder()
                    .right(Types.Right.Call)
                    .strike(BigDecimal.valueOf(15))
                    .contractId(1)
                    .build();
    ContractDbo contract2 =
            ContractDbo.builder()
                    .right(Types.Right.Call)
                    .strike(BigDecimal.valueOf(10))
                    .contractId(2)
                    .build();
    ContractDbo contract3 =
            ContractDbo.builder()
                    .right(Types.Right.Put)
                    .strike(BigDecimal.valueOf(10))
                    .contractId(3)
                    .build();
    ContractDbo contract4 =
            ContractDbo.builder()
                    .right(Types.Right.Put)
                    .strike(BigDecimal.valueOf(5))
                    .contractId(4)
                    .build();
    when(contractRepository.findFirstByContractId(1)).thenReturn(Optional.of(contract1));
    when(contractRepository.findFirstByContractId(2)).thenReturn(Optional.of(contract2));
    when(contractRepository.findFirstByContractId(3)).thenReturn(Optional.of(contract3));
    when(contractRepository.findFirstByContractId(4)).thenReturn(Optional.of(contract4));

    ComboLegDbo leg = ComboLegDbo.builder().contractId(1).ratio(1).action(Types.Action.SELL).build();
    ComboLegDbo leg2 =
            ComboLegDbo.builder().contractId(2).ratio(1).action(Types.Action.BUY).build();
    ComboLegDbo leg3 =
            ComboLegDbo.builder().contractId(3).ratio(1).action(Types.Action.BUY).build();
    ComboLegDbo leg4 =
            ComboLegDbo.builder().contractId(4).ratio(1).action(Types.Action.SELL).build();

    assertThat(strategyNameService.resolveStrategyFromComboLegs(List.of(leg, leg2, leg3, leg4)))
            .isEqualTo(Strategy.SHORT_IRON_BUTTERFLY);
  }

  @Test
  void getStrategyNameFromComboLegsService_WithIronCondor() {
    ContractDbo contract1 =
        ContractDbo.builder()
            .right(Types.Right.Call)
            .strike(BigDecimal.valueOf(15))
            .contractId(1)
            .build();
    ContractDbo contract2 =
        ContractDbo.builder()
            .right(Types.Right.Call)
            .strike(BigDecimal.valueOf(10))
            .contractId(2)
            .build();
    ContractDbo contract3 =
        ContractDbo.builder()
            .right(Types.Right.Put)
            .strike(BigDecimal.valueOf(9))
            .contractId(3)
            .build();
    ContractDbo contract4 =
        ContractDbo.builder()
            .right(Types.Right.Put)
            .strike(BigDecimal.valueOf(5))
            .contractId(4)
            .build();
    when(contractRepository.findFirstByContractId(1)).thenReturn(Optional.of(contract1));
    when(contractRepository.findFirstByContractId(2)).thenReturn(Optional.of(contract2));
    when(contractRepository.findFirstByContractId(3)).thenReturn(Optional.of(contract3));
    when(contractRepository.findFirstByContractId(4)).thenReturn(Optional.of(contract4));

    ComboLegDbo leg = ComboLegDbo.builder().contractId(1).ratio(1).action(Types.Action.BUY).build();
    ComboLegDbo leg2 =
        ComboLegDbo.builder().contractId(2).ratio(1).action(Types.Action.SELL).build();
    ComboLegDbo leg3 =
        ComboLegDbo.builder().contractId(3).ratio(1).action(Types.Action.SELL).build();
    ComboLegDbo leg4 =
        ComboLegDbo.builder().contractId(4).ratio(1).action(Types.Action.BUY).build();

    assertThat(strategyNameService.resolveStrategyFromComboLegs(List.of(leg, leg2, leg3, leg4)))
        .isEqualTo(Strategy.IRON_CONDOR);
  }
  @Test
  void getStrategyNameFromComboLegsService_WithShortIronCondor() {
    ContractDbo contract1 =
            ContractDbo.builder()
                    .right(Types.Right.Call)
                    .strike(BigDecimal.valueOf(10))
                    .contractId(1)
                    .build();
    ContractDbo contract2 =
            ContractDbo.builder()
                    .right(Types.Right.Call)
                    .strike(BigDecimal.valueOf(15))
                    .contractId(2)
                    .build();
    ContractDbo contract3 =
            ContractDbo.builder()
                    .right(Types.Right.Put)
                    .strike(BigDecimal.valueOf(5))
                    .contractId(3)
                    .build();
    ContractDbo contract4 =
            ContractDbo.builder()
                    .right(Types.Right.Put)
                    .strike(BigDecimal.valueOf(9))
                    .contractId(4)
                    .build();
    when(contractRepository.findFirstByContractId(1)).thenReturn(Optional.of(contract1));
    when(contractRepository.findFirstByContractId(2)).thenReturn(Optional.of(contract2));
    when(contractRepository.findFirstByContractId(3)).thenReturn(Optional.of(contract3));
    when(contractRepository.findFirstByContractId(4)).thenReturn(Optional.of(contract4));

    ComboLegDbo leg = ComboLegDbo.builder().contractId(1).ratio(1).action(Types.Action.BUY).build();
    ComboLegDbo leg2 =
            ComboLegDbo.builder().contractId(2).ratio(1).action(Types.Action.SELL).build();
    ComboLegDbo leg3 =
            ComboLegDbo.builder().contractId(3).ratio(1).action(Types.Action.SELL).build();
    ComboLegDbo leg4 =
            ComboLegDbo.builder().contractId(4).ratio(1).action(Types.Action.BUY).build();

    assertThat(strategyNameService.resolveStrategyFromComboLegs(List.of(leg, leg2, leg3, leg4)))
            .isEqualTo(Strategy.SHORT_IRON_CONDOR);
  }
  @Test
  @Disabled
  //TODO this still needs to be implemented, but seems not too urgent
  void getStrategyNameFromComboLegsService_WithFalseIronCondor_ShouldReturnCustomStrategy() {
    ContractDbo contract1 =
            ContractDbo.builder()
                    .right(Types.Right.Call)
                    .strike(BigDecimal.valueOf(10))
                    .contractId(1)
                    .build();
    ContractDbo contract2 =
            ContractDbo.builder()
                    .right(Types.Right.Call)
                    .strike(BigDecimal.valueOf(15))
                    .contractId(2)
                    .build();
    ContractDbo contract3 =
            ContractDbo.builder()
                    .right(Types.Right.Put)
                    .strike(BigDecimal.valueOf(5))
                    .contractId(3)
                    .build();
    ContractDbo contract4 =
            ContractDbo.builder()
                    .right(Types.Right.Put)
                    .strike(BigDecimal.valueOf(11))
                    .contractId(4)
                    .build();
    when(contractRepository.findFirstByContractId(1)).thenReturn(Optional.of(contract1));
    when(contractRepository.findFirstByContractId(2)).thenReturn(Optional.of(contract2));
    when(contractRepository.findFirstByContractId(3)).thenReturn(Optional.of(contract3));
    when(contractRepository.findFirstByContractId(4)).thenReturn(Optional.of(contract4));

    ComboLegDbo leg = ComboLegDbo.builder().contractId(1).ratio(1).action(Types.Action.BUY).build();
    ComboLegDbo leg2 =
            ComboLegDbo.builder().contractId(2).ratio(1).action(Types.Action.SELL).build();
    ComboLegDbo leg3 =
            ComboLegDbo.builder().contractId(3).ratio(1).action(Types.Action.SELL).build();
    ComboLegDbo leg4 =
            ComboLegDbo.builder().contractId(4).ratio(1).action(Types.Action.BUY).build();

    assertThat(strategyNameService.resolveStrategyFromComboLegs(List.of(leg, leg2, leg3, leg4)))
            .isEqualTo(Strategy.CUSTOM_STRATEGY);
  }
  @Test
  void getStrategyNameFromComboLegsService_BullSpread() {
    ContractDbo contract1 =
            ContractDbo.builder()
                    .right(Types.Right.Call)
                    .strike(BigDecimal.valueOf(10))
                    .contractId(1)
                    .build();
    ContractDbo contract2 =
            ContractDbo.builder()
                    .right(Types.Right.Call)
                    .strike(BigDecimal.valueOf(15))
                    .contractId(2)
                    .build();
    ContractDbo contract3 =
            ContractDbo.builder()
                    .right(Types.Right.Put)
                    .strike(BigDecimal.valueOf(11))
                    .contractId(3)
                    .build();
    ContractDbo contract4 =
            ContractDbo.builder()
                    .right(Types.Right.Put)
                    .strike(BigDecimal.valueOf(9))
                    .contractId(4)
                    .build();
    when(contractRepository.findFirstByContractId(1)).thenReturn(Optional.of(contract1));
    when(contractRepository.findFirstByContractId(2)).thenReturn(Optional.of(contract2));
    when(contractRepository.findFirstByContractId(3)).thenReturn(Optional.of(contract3));
    when(contractRepository.findFirstByContractId(4)).thenReturn(Optional.of(contract4));

    ComboLegDbo leg = ComboLegDbo.builder().contractId(1).ratio(1).action(Types.Action.BUY).build();
    ComboLegDbo leg2 =
            ComboLegDbo.builder().contractId(2).ratio(1).action(Types.Action.SELL).build();
    ComboLegDbo leg3 =
            ComboLegDbo.builder().contractId(3).ratio(1).action(Types.Action.SELL).build();
    ComboLegDbo leg4 =
            ComboLegDbo.builder().contractId(4).ratio(1).action(Types.Action.BUY).build();

    assertThat(strategyNameService.resolveStrategyFromComboLegs(List.of(leg, leg2)))
            .isEqualTo(Strategy.BULL_SPREAD);
    assertThat(strategyNameService.resolveStrategyFromComboLegs(List.of(leg3, leg4)))
            .isEqualTo(Strategy.BULL_SPREAD);
  }
  @Test
  void getStrategyNameFromComboLegsService_BearSpread() {
    ContractDbo contract1 =
            ContractDbo.builder()
                    .right(Types.Right.Call)
                    .strike(BigDecimal.valueOf(16))
                    .contractId(1)
                    .build();
    ContractDbo contract2 =
            ContractDbo.builder()
                    .right(Types.Right.Call)
                    .strike(BigDecimal.valueOf(15))
                    .contractId(2)
                    .build();
    ContractDbo contract3 =
            ContractDbo.builder()
                    .right(Types.Right.Put)
                    .strike(BigDecimal.valueOf(8))
                    .contractId(3)
                    .build();
    ContractDbo contract4 =
            ContractDbo.builder()
                    .right(Types.Right.Put)
                    .strike(BigDecimal.valueOf(9))
                    .contractId(4)
                    .build();
    when(contractRepository.findFirstByContractId(1)).thenReturn(Optional.of(contract1));
    when(contractRepository.findFirstByContractId(2)).thenReturn(Optional.of(contract2));
    when(contractRepository.findFirstByContractId(3)).thenReturn(Optional.of(contract3));
    when(contractRepository.findFirstByContractId(4)).thenReturn(Optional.of(contract4));

    ComboLegDbo leg = ComboLegDbo.builder().contractId(1).ratio(1).action(Types.Action.BUY).build();
    ComboLegDbo leg2 =
            ComboLegDbo.builder().contractId(2).ratio(1).action(Types.Action.SELL).build();
    ComboLegDbo leg3 =
            ComboLegDbo.builder().contractId(3).ratio(1).action(Types.Action.SELL).build();
    ComboLegDbo leg4 =
            ComboLegDbo.builder().contractId(4).ratio(1).action(Types.Action.BUY).build();

    assertThat(strategyNameService.resolveStrategyFromComboLegs(List.of(leg, leg2)))
            .isEqualTo(Strategy.BEAR_SPREAD);
    assertThat(strategyNameService.resolveStrategyFromComboLegs(List.of(leg3, leg4)))
            .isEqualTo(Strategy.BEAR_SPREAD);
  }
}
