package de.segoy.springboottradingdata.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

import com.ib.client.OrderStatus;
import com.ib.client.OrderType;
import com.ib.client.Types;
import de.segoy.springboottradingdata.model.data.entity.ComboLegDbo;
import de.segoy.springboottradingdata.model.data.entity.ContractDbo;
import de.segoy.springboottradingdata.model.data.entity.OrderDbo;
import de.segoy.springboottradingdata.repository.ContractRepository;
import de.segoy.springboottradingdata.repository.OrderRepository;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PartialComboOrderFinderTest {

  @Mock private ContractRepository contractRepository;
  @Mock private OrderRepository orderRepository;
  @InjectMocks private PartialComboOrderFinder partialComboOrderFinder;

  @Test
  void findMostExpectedCase_WithNoOverlappingStrategiesAndSingleOrders() {
    ComboLegDbo leg1 = ComboLegDbo.builder().contractId(1).build();
    ComboLegDbo leg2 = ComboLegDbo.builder().contractId(2).build();
    ComboLegDbo leg3 = ComboLegDbo.builder().contractId(3).build();
    ComboLegDbo leg4 = ComboLegDbo.builder().contractId(4).build();
    ComboLegDbo leg5 = ComboLegDbo.builder().contractId(5).build();
    ComboLegDbo leg6 = ComboLegDbo.builder().contractId(6).build();
    ComboLegDbo leg7 = ComboLegDbo.builder().contractId(7).build();
    ComboLegDbo leg8 = ComboLegDbo.builder().contractId(8).build();
    List<ComboLegDbo> legs = List.of(leg1, leg2, leg3, leg4, leg5, leg6, leg7, leg8);
    ContractDbo totalPosition = ContractDbo.builder().comboLegs(legs).build();

    List<ComboLegDbo> stratLegs1 = List.of(leg1, leg2, leg3, leg4);
    ContractDbo strategy1 = ContractDbo.builder().comboLegs(stratLegs1).build();
    List<ComboLegDbo> stratLegs2 = List.of(leg5, leg6, leg7, leg8);
    ContractDbo strategy2 = ContractDbo.builder().comboLegs(stratLegs2).build();

    OrderDbo order1 =
        OrderDbo.builder()
            .orderType(OrderType.MKT)
            .contractDBO(strategy1)
            .id(1L)
            .action(Types.Action.BUY)
            .status(OrderStatus.Filled)
            .build();
    OrderDbo order2 =
            OrderDbo.builder()
                    .orderType(OrderType.MKT)
                    .contractDBO(strategy1)
                    .id(2L)
                    .action(Types.Action.BUY)
                    .status(OrderStatus.Filled)
                    .build();

    when(contractRepository.findByComboLegsDescriptionContains("1")).thenReturn(List.of(strategy1));
    when(contractRepository.findByComboLegsDescriptionContains("5")).thenReturn(List.of(strategy2));
    when(contractRepository.findByComboLegsDescriptionContains("2")).thenReturn(List.of(strategy1));
    when(contractRepository.findByComboLegsDescriptionContains("3")).thenReturn(List.of(strategy1));
    when(contractRepository.findByComboLegsDescriptionContains("4")).thenReturn(List.of(strategy1));
    when(contractRepository.findByComboLegsDescriptionContains("6")).thenReturn(List.of(strategy2));
    when(contractRepository.findByComboLegsDescriptionContains("7")).thenReturn(List.of(strategy2));
    when(contractRepository.findByComboLegsDescriptionContains("8")).thenReturn(List.of(strategy2));

    when(orderRepository.findByContractDBO(strategy1)).thenReturn(List.of(order1));
    when(orderRepository.findByContractDBO(strategy2)).thenReturn(List.of(order2));

    when(orderRepository.findByContractDBO(strategy1)).thenReturn(List.of(order1));

    List<OrderDbo> instanceUnderTest =
        partialComboOrderFinder.findExistingStrategyContractsInCombo(totalPosition);

    assertThat(instanceUnderTest).isEqualTo(List.of(order1, order2));
  }

  @Test
  void findLessExpectedCase_WithOverlappingStrategiesAndSingleOrders() {
    ComboLegDbo leg1 = ComboLegDbo.builder().contractId(1).build();
    ComboLegDbo leg2 = ComboLegDbo.builder().contractId(2).build();
    ComboLegDbo leg3 = ComboLegDbo.builder().contractId(3).build();
    ComboLegDbo leg4 = ComboLegDbo.builder().contractId(4).build();
    ComboLegDbo leg5 = ComboLegDbo.builder().contractId(5).build();
    ComboLegDbo leg6 = ComboLegDbo.builder().contractId(6).build();
    ComboLegDbo leg7 = ComboLegDbo.builder().contractId(7).build();
    ComboLegDbo leg8 = ComboLegDbo.builder().contractId(8).build();
    List<ComboLegDbo> legs = List.of(leg1, leg2, leg3, leg4, leg5, leg6, leg7, leg8);
    ContractDbo totalPosition = ContractDbo.builder().comboLegs(legs).build();

    List<ComboLegDbo> stratLegs1 = List.of(leg1, leg2, leg3, leg4);
    ContractDbo strategy1 = ContractDbo.builder().comboLegs(stratLegs1).build();
    List<ComboLegDbo> stratLegs2 = List.of(leg4, leg2, leg7, leg8);
    ContractDbo strategy2 = ContractDbo.builder().comboLegs(stratLegs2).build();

    OrderDbo order1 =
            OrderDbo.builder()
                    .orderType(OrderType.MKT)
                    .contractDBO(strategy1)
                    .id(1L)
                    .action(Types.Action.BUY)
                    .status(OrderStatus.Filled)
                    .build();
    OrderDbo order2 =
            OrderDbo.builder()
                    .orderType(OrderType.MKT)
                    .contractDBO(strategy1)
                    .id(2L)
                    .action(Types.Action.BUY)
                    .status(OrderStatus.Filled)
                    .build();

    when(contractRepository.findByComboLegsDescriptionContains("1")).thenReturn(List.of(strategy1));
    when(contractRepository.findByComboLegsDescriptionContains("5"))
        .thenReturn(Collections.emptyList());
    when(contractRepository.findByComboLegsDescriptionContains("2"))
        .thenReturn(List.of(strategy1, strategy2));
    when(contractRepository.findByComboLegsDescriptionContains("3")).thenReturn(List.of(strategy1));
    when(contractRepository.findByComboLegsDescriptionContains("4"))
        .thenReturn(List.of(strategy1, strategy2));
    when(contractRepository.findByComboLegsDescriptionContains("6"))
        .thenReturn(Collections.emptyList());
    when(contractRepository.findByComboLegsDescriptionContains("7")).thenReturn(List.of(strategy2));
    when(contractRepository.findByComboLegsDescriptionContains("8")).thenReturn(List.of(strategy2));

    when(orderRepository.findByContractDBO(strategy1)).thenReturn(List.of(order1));
    when(orderRepository.findByContractDBO(strategy2)).thenReturn(List.of(order2));

    List<OrderDbo> instanceUnderTest =
        partialComboOrderFinder.findExistingStrategyContractsInCombo(totalPosition);

    assertThat(instanceUnderTest).isEqualTo(List.of(order1, order2));
  }

  @Test
  void findLessExpectedCase_WithOverlappingStrategiesAndMultipleNotFilledOrders() {
    ComboLegDbo leg1 = ComboLegDbo.builder().contractId(1).build();
    ComboLegDbo leg2 = ComboLegDbo.builder().contractId(2).build();
    ComboLegDbo leg3 = ComboLegDbo.builder().contractId(3).build();
    ComboLegDbo leg4 = ComboLegDbo.builder().contractId(4).build();
    ComboLegDbo leg5 = ComboLegDbo.builder().contractId(5).build();
    ComboLegDbo leg6 = ComboLegDbo.builder().contractId(6).build();
    ComboLegDbo leg7 = ComboLegDbo.builder().contractId(7).build();
    ComboLegDbo leg8 = ComboLegDbo.builder().contractId(8).build();
    List<ComboLegDbo> legs = List.of(leg1, leg2, leg3, leg4, leg5, leg6, leg7, leg8);
    ContractDbo totalPosition = ContractDbo.builder().comboLegs(legs).build();

    List<ComboLegDbo> stratLegs1 = List.of(leg1, leg2, leg3, leg4);
    ContractDbo strategy1 = ContractDbo.builder().comboLegs(stratLegs1).build();
    List<ComboLegDbo> stratLegs2 = List.of(leg4, leg2, leg7, leg8);
    ContractDbo strategy2 = ContractDbo.builder().comboLegs(stratLegs2).build();

    OrderDbo order1 =
            OrderDbo.builder()
                    .orderType(OrderType.MKT)
                    .contractDBO(strategy1)
                    .id(1L)
                    .action(Types.Action.BUY)
                    .status(OrderStatus.Filled)
                    .build();
    OrderDbo order2 =
            OrderDbo.builder()
                    .orderType(OrderType.MKT)
                    .contractDBO(strategy1)
                    .id(2L)
                    .action(Types.Action.BUY)
                    .status(OrderStatus.Filled)
                    .build();
    OrderDbo order3 =
            OrderDbo.builder()
                    .orderType(OrderType.MKT)
                    .contractDBO(strategy1)
                    .id(2L)
                    .action(Types.Action.BUY)
                    .status(OrderStatus.Submitted)
                    .build();
    OrderDbo order4 =
            OrderDbo.builder()
                    .orderType(OrderType.MKT)
                    .contractDBO(strategy1)
                    .id(2L)
                    .action(Types.Action.BUY)
                    .status(OrderStatus.Cancelled)
                    .build();

    when(contractRepository.findByComboLegsDescriptionContains("1")).thenReturn(List.of(strategy1));
    when(contractRepository.findByComboLegsDescriptionContains("5"))
            .thenReturn(Collections.emptyList());
    when(contractRepository.findByComboLegsDescriptionContains("2"))
            .thenReturn(List.of(strategy1, strategy2));
    when(contractRepository.findByComboLegsDescriptionContains("3")).thenReturn(List.of(strategy1));
    when(contractRepository.findByComboLegsDescriptionContains("4"))
            .thenReturn(List.of(strategy1, strategy2));
    when(contractRepository.findByComboLegsDescriptionContains("6"))
            .thenReturn(Collections.emptyList());
    when(contractRepository.findByComboLegsDescriptionContains("7")).thenReturn(List.of(strategy2));
    when(contractRepository.findByComboLegsDescriptionContains("8")).thenReturn(List.of(strategy2));

    when(orderRepository.findByContractDBO(strategy1)).thenReturn(List.of(order1));
    when(orderRepository.findByContractDBO(strategy2)).thenReturn(List.of(order2, order3, order4));

    List<OrderDbo> instanceUnderTest =
            partialComboOrderFinder.findExistingStrategyContractsInCombo(totalPosition);

    assertThat(instanceUnderTest).isEqualTo(List.of(order1, order2));
  }
  @Test
  void findLessExpectedCase_WithOverlappingStrategiesAndMultipleFilledOrders() {
    ComboLegDbo leg1 = ComboLegDbo.builder().contractId(1).build();
    ComboLegDbo leg2 = ComboLegDbo.builder().contractId(2).build();
    ComboLegDbo leg3 = ComboLegDbo.builder().contractId(3).build();
    ComboLegDbo leg4 = ComboLegDbo.builder().contractId(4).build();
    ComboLegDbo leg5 = ComboLegDbo.builder().contractId(5).build();
    ComboLegDbo leg6 = ComboLegDbo.builder().contractId(6).build();
    ComboLegDbo leg7 = ComboLegDbo.builder().contractId(7).build();
    ComboLegDbo leg8 = ComboLegDbo.builder().contractId(8).build();
    List<ComboLegDbo> legs = List.of(leg1, leg2, leg3, leg4, leg5, leg6, leg7, leg8);
    ContractDbo totalPosition = ContractDbo.builder().comboLegs(legs).build();

    List<ComboLegDbo> stratLegs1 = List.of(leg1, leg2, leg3, leg4);
    ContractDbo strategy1 = ContractDbo.builder().comboLegs(stratLegs1).build();
    List<ComboLegDbo> stratLegs2 = List.of(leg4, leg2, leg7, leg8);
    ContractDbo strategy2 = ContractDbo.builder().comboLegs(stratLegs2).build();

    OrderDbo order1 =
            OrderDbo.builder()
                    .orderType(OrderType.MKT)
                    .contractDBO(strategy1)
                    .id(1L)
                    .action(Types.Action.BUY)
                    .status(OrderStatus.Filled)
                    .build();
    OrderDbo order2 =
            OrderDbo.builder()
                    .orderType(OrderType.MKT)
                    .contractDBO(strategy1)
                    .id(2L)
                    .action(Types.Action.BUY)
                    .status(OrderStatus.Filled)
                    .build();
    OrderDbo order3 =
            OrderDbo.builder()
                    .orderType(OrderType.MKT)
                    .contractDBO(strategy1)
                    .id(2L)
                    .action(Types.Action.SELL)
                    .status(OrderStatus.Filled)
                    .build();
    OrderDbo order4 =
            OrderDbo.builder()
                    .orderType(OrderType.LMT)
                    .contractDBO(strategy1)
                    .id(2L)
                    .action(Types.Action.BUY)
                    .status(OrderStatus.Filled)
                    .build();

    when(contractRepository.findByComboLegsDescriptionContains("1")).thenReturn(List.of(strategy1));
    when(contractRepository.findByComboLegsDescriptionContains("5"))
            .thenReturn(Collections.emptyList());
    when(contractRepository.findByComboLegsDescriptionContains("2"))
            .thenReturn(List.of(strategy1, strategy2));
    when(contractRepository.findByComboLegsDescriptionContains("3")).thenReturn(List.of(strategy1));
    when(contractRepository.findByComboLegsDescriptionContains("4"))
            .thenReturn(List.of(strategy1, strategy2));
    when(contractRepository.findByComboLegsDescriptionContains("6"))
            .thenReturn(Collections.emptyList());
    when(contractRepository.findByComboLegsDescriptionContains("7")).thenReturn(List.of(strategy2));
    when(contractRepository.findByComboLegsDescriptionContains("8")).thenReturn(List.of(strategy2));

    when(orderRepository.findByContractDBO(strategy1)).thenReturn(List.of(order1));
    when(orderRepository.findByContractDBO(strategy2)).thenReturn(List.of(order2, order3, order4));

    List<OrderDbo> instanceUnderTest =
            partialComboOrderFinder.findExistingStrategyContractsInCombo(totalPosition);

    assertThat(instanceUnderTest).isEqualTo(List.of(order1, order2, order3, order4));
  }
}
