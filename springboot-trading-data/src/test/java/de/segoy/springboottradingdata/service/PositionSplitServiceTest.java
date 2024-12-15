package de.segoy.springboottradingdata.service;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

import com.ib.client.OrderStatus;
import com.ib.client.Types;
import de.segoy.springboottradingdata.model.data.entity.ComboLegDbo;
import de.segoy.springboottradingdata.model.data.entity.ContractDbo;
import de.segoy.springboottradingdata.model.data.entity.OrderDbo;
import de.segoy.springboottradingdata.model.data.entity.PositionDbo;
import de.segoy.springboottradingdata.model.subtype.Symbol;
import de.segoy.springboottradingdata.modelsynchronize.PositionDataDatabaseSynchronizer;
import de.segoy.springboottradingdata.optionstradingservice.AutoTradeIdService;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

@ExtendWith(MockitoExtension.class)
class PositionSplitServiceTest {

  public static final String TRADE_DATE = "20241205";
  @Mock private StrategyComboLegsDescriptionCreator strategyComboLegsDescriptionCreator;
  @Mock private AutoTradeIdService autoTradeIdService;
  @Mock private PositionDataDatabaseSynchronizer positionDataDatabaseSynchronizer;
  @InjectMocks private PositionSplitService positionSplitService;

  @BeforeEach
  void setup() {
        when(positionDataDatabaseSynchronizer.updateInDbOrSave(any(PositionDbo.class)))
                .thenAnswer((Answer<Optional<PositionDbo>>) invocation -> Optional.of(invocation.getArgument(0)));
    when(autoTradeIdService.setIdForAutoTrade(any())).thenReturn(null);
  }

  @Test
  void testSplit_withStrategiesDifferentAmountsAndOneLeftoverWithNegativePosition_first1Second3() {

    ComboLegDbo positionLeg1 = createLeg(1, 4, Types.Action.BUY);
    ComboLegDbo positionLeg2 = createLeg(2, 4, Types.Action.BUY);
    ComboLegDbo positionLeg3 = createLeg(3, 1, Types.Action.SELL);
    ComboLegDbo positionLeg4 = createLeg(4, 1, Types.Action.SELL);
    ComboLegDbo positionLeg5 = createLeg(5, 1, Types.Action.SELL);
    ComboLegDbo contract1Leg1 = createLeg(1, 1, Types.Action.BUY);
    ComboLegDbo contract1Leg2 = createLeg(2, 1, Types.Action.BUY);
    ComboLegDbo contract1Leg3 = createLeg(3, 1, Types.Action.SELL);
    ComboLegDbo contract1Leg4 = createLeg(4, 1, Types.Action.SELL);
    ComboLegDbo contract2Leg1 = createLeg(1, 1, Types.Action.BUY);
    ComboLegDbo contract2Leg2 = createLeg(2, 1, Types.Action.BUY);

    // Positions

    List<ComboLegDbo> legs =
        List.of(positionLeg1, positionLeg2, positionLeg3, positionLeg4, positionLeg5);
    List<ComboLegDbo> contract1Legs =
        List.of(contract1Leg1, contract1Leg2, contract1Leg3, contract1Leg4);
    List<ComboLegDbo> contract2Legs = List.of(contract2Leg1, contract2Leg2);
    ContractDbo positionContract = createBagContract(legs);
    PositionDbo position =
        PositionDbo.builder()
            .position(getBigDecimal(1))
            .totalCost(getBigDecimal(59))
            .averageCost(getBigDecimal(59))
            .contractDBO(positionContract)
            .build();
    ContractDbo dbContract1 = createBagContract(contract1Legs);
    ContractDbo dbContract2 = createBagContract(contract2Legs);

    OrderDbo order1 =
        OrderDbo.builder()
            .contractDBO(dbContract1)
            .status(OrderStatus.Filled)
            .action(Types.Action.BUY)
            .totalQuantity(getBigDecimal(1))
            .avgFillPrice(getBigDecimal(12))
            .lastModified(LocalDateTime.of(2024, 12, 6, 15, 30))
            .build();
    OrderDbo order2 =
        OrderDbo.builder()
            .contractDBO(dbContract2)
            .status(OrderStatus.Filled)
            .action(Types.Action.BUY)
            .totalQuantity(getBigDecimal(1))
            .avgFillPrice(getBigDecimal(14))
            .lastModified(LocalDateTime.of(2024, 12, 6, 15, 30))
            .build();
    OrderDbo order3 =
        OrderDbo.builder()
            .contractDBO(dbContract2)
            .status(OrderStatus.Filled)
            .action(Types.Action.BUY)
            .totalQuantity(getBigDecimal(2))
            .avgFillPrice(getBigDecimal(17))
            .lastModified(LocalDateTime.of(2024, 12, 6, 15, 30))
            .build();

    List<PositionDbo> instanceUnderTest =
        positionSplitService.splitGivenContractsFromPosition(
            List.of(order1, order2, order3), position);

    assertThat(instanceUnderTest)
        .containsAll(
            List.of(
                PositionDbo.builder()
                    .averageCost(getBigDecimal(16))
                    .totalCost(getBigDecimal(48))
                    .position(getBigDecimal(3))
                    .contractDBO(order2.getContractDBO())
                    .build(),
                PositionDbo.builder()
                    .averageCost(getBigDecimal(12))
                    .totalCost(getBigDecimal(12))
                    .position(getBigDecimal(1))
                    .contractDBO(order1.getContractDBO())
                    .build()));
    assertThat(instanceUnderTest.size()).isEqualTo(3);
    assertThat(instanceUnderTest.get(2).getPosition()).isEqualTo(getBigDecimal(1));
    assertThat(instanceUnderTest.get(2).getTotalCost()).isEqualTo(getBigDecimal(-1));
    assertThat(instanceUnderTest.get(2).getAverageCost()).isEqualTo(getBigDecimal(-1));
    assertThat(instanceUnderTest.get(2).getContractDBO().getLastTradeDate()).isEqualTo("20241205");
    assertThat(instanceUnderTest.get(2).getContractDBO().getComboLegs().size()).isEqualTo(1);
    assertThat(instanceUnderTest.get(2).getContractDBO().getComboLegs().get(0))
        .isEqualTo(positionLeg5);
    assertThat(
            instanceUnderTest.stream()
                .map(PositionDbo::getTotalCost)
                .reduce(BigDecimal.ZERO, BigDecimal::add))
        .isEqualTo(position.getTotalCost());
  }

  private static BigDecimal getBigDecimal(double i) {
    return BigDecimal.valueOf(i).setScale(2, RoundingMode.HALF_UP);
  }

  @Test
  void testSplit_withStrategiesDifferentAmountsAndTwoLeftoverWithPositivePosition_first1Second3() {
    ComboLegDbo positionLeg1 = createLeg(1, 4, Types.Action.BUY);
    ComboLegDbo positionLeg2 = createLeg(2, 4, Types.Action.BUY);
    ComboLegDbo positionLeg3 = createLeg(3, 1, Types.Action.SELL);
    ComboLegDbo positionLeg4 = createLeg(4, 1, Types.Action.SELL);
    ComboLegDbo contract1Leg1 = createLeg(1, 1, Types.Action.BUY);
    ComboLegDbo contract1Leg2 = createLeg(2, 1, Types.Action.BUY);
    ComboLegDbo contract1Leg3 = createLeg(3, 1, Types.Action.SELL);
    ComboLegDbo contract1Leg4 = createLeg(4, 1, Types.Action.SELL);
    ComboLegDbo contract2Leg1 = createLeg(1, 1, Types.Action.BUY);
    ComboLegDbo contract2Leg2 = createLeg(2, 1, Types.Action.BUY);

    ComboLegDbo positionLeg5 = createLeg(5, 2, Types.Action.SELL);
    ComboLegDbo positionLeg6 = createLeg(6, 2, Types.Action.BUY);

    List<ComboLegDbo> legs =
        List.of(positionLeg1, positionLeg2, positionLeg3, positionLeg4, positionLeg5, positionLeg6);
    List<ComboLegDbo> contract1Legs =
        List.of(contract1Leg1, contract1Leg2, contract1Leg3, contract1Leg4);
    List<ComboLegDbo> contract2Legs = List.of(contract2Leg1, contract2Leg2);
    ContractDbo positionContract = createBagContract(legs);
    PositionDbo position =
        PositionDbo.builder()
            .position(getBigDecimal(1))
            .totalCost(getBigDecimal(59))
            .averageCost(getBigDecimal(59))
            .contractDBO(positionContract)
            .build();
    ContractDbo dbContract1 = createBagContract(contract1Legs);
    ContractDbo dbContract2 = createBagContract(contract2Legs);

    OrderDbo order1 =
        OrderDbo.builder()
            .contractDBO(dbContract1)
            .status(OrderStatus.Filled)
            .action(Types.Action.BUY)
            .totalQuantity(getBigDecimal(1))
            .avgFillPrice(getBigDecimal(12))
            .lastModified(LocalDateTime.of(2024, 12, 6, 15, 30))
            .build();
    OrderDbo order2 =
        OrderDbo.builder()
            .contractDBO(dbContract2)
            .status(OrderStatus.Filled)
            .action(Types.Action.BUY)
            .totalQuantity(getBigDecimal(1))
            .avgFillPrice(getBigDecimal(14))
            .lastModified(LocalDateTime.of(2024, 12, 6, 15, 32))
            .build();
    OrderDbo order3 =
        OrderDbo.builder()
            .contractDBO(dbContract2)
            .status(OrderStatus.Filled)
            .action(Types.Action.BUY)
            .totalQuantity(getBigDecimal(2))
            .avgFillPrice(getBigDecimal(17))
            .lastModified(LocalDateTime.of(2024, 12, 6, 15, 31))
            .build();

    when(strategyComboLegsDescriptionCreator.generateComboLegsDescription(any())).thenReturn("");

    List<PositionDbo> instanceUnderTest =
        positionSplitService.splitGivenContractsFromPosition(
            List.of(order1, order2, order3), position);

    PositionDbo pos1 =
        PositionDbo.builder()
            .averageCost(getBigDecimal(16))
            .totalCost(getBigDecimal(48))
            .position(getBigDecimal(3))
            .contractDBO(order2.getContractDBO())
            .build();
    PositionDbo pos2 =
        PositionDbo.builder()
            .averageCost(getBigDecimal(12))
            .totalCost(getBigDecimal(12))
            .position(getBigDecimal(1))
            .contractDBO(order1.getContractDBO())
            .build();

    assertThat(instanceUnderTest).containsAll(List.of(pos1, pos2));
    assertThat(instanceUnderTest.size()).isEqualTo(3);
    assertThat(instanceUnderTest.get(2).getPosition()).isEqualTo(getBigDecimal(2));
    assertThat(instanceUnderTest.get(2).getTotalCost()).isEqualTo(getBigDecimal(-1));
    assertThat(instanceUnderTest.get(2).getAverageCost()).isEqualTo(getBigDecimal(-0.5));
    assertThat(instanceUnderTest.get(2).getContractDBO().getLastTradeDate()).isEqualTo("20241205");
    assertThat(instanceUnderTest.get(2).getContractDBO().getComboLegs().size()).isEqualTo(2);
    assertThat(instanceUnderTest.get(2).getContractDBO().getComboLegs())
        .containsAll(List.of(positionLeg5, positionLeg6));
    assertThat(
            instanceUnderTest.stream()
                .map(PositionDbo::getTotalCost)
                .reduce(BigDecimal.ZERO, BigDecimal::add))
        .isEqualTo(position.getTotalCost());
  }

  @Test
  void
      testSplit_withStrategiesDifferentAmountsAndNoneLeftoverAndSellingAndRebuyingOrders_first1Second3() {
    when(autoTradeIdService.setIdForAutoTrade(any())).thenReturn(null);
    ComboLegDbo positionLeg1 = createLeg(1, 4, Types.Action.BUY);
    ComboLegDbo positionLeg2 = createLeg(2, 4, Types.Action.BUY);
    ComboLegDbo positionLeg3 = createLeg(3, 1, Types.Action.SELL);
    ComboLegDbo positionLeg4 = createLeg(4, 1, Types.Action.SELL);
    ComboLegDbo contract1Leg1 = createLeg(1, 1, Types.Action.BUY);
    ComboLegDbo contract1Leg2 = createLeg(2, 1, Types.Action.BUY);
    ComboLegDbo contract1Leg3 = createLeg(3, 1, Types.Action.SELL);
    ComboLegDbo contract1Leg4 = createLeg(4, 1, Types.Action.SELL);
    ComboLegDbo contract2Leg1 = createLeg(1, 1, Types.Action.BUY);
    ComboLegDbo contract2Leg2 = createLeg(2, 1, Types.Action.BUY);

    List<ComboLegDbo> legs = List.of(positionLeg1, positionLeg2, positionLeg3, positionLeg4);
    List<ComboLegDbo> contract1Legs =
        List.of(contract1Leg1, contract1Leg2, contract1Leg3, contract1Leg4);
    List<ComboLegDbo> contract2Legs = List.of(contract2Leg1, contract2Leg2);
    ContractDbo positionContract = createBagContract(legs);
    PositionDbo position =
        PositionDbo.builder()
            .position(getBigDecimal(1))
            .totalCost(getBigDecimal(61))
            .averageCost(getBigDecimal(61))
            .contractDBO(positionContract)
            .build();
    ContractDbo dbContract1 = createBagContract(contract1Legs);
    ContractDbo dbContract2 = createBagContract(contract2Legs);

    OrderDbo order1 =
        OrderDbo.builder()
            .contractDBO(dbContract1)
            .status(OrderStatus.Filled)
            .action(Types.Action.BUY)
            .totalQuantity(getBigDecimal(1))
            .avgFillPrice(getBigDecimal(12))
            .lastModified(LocalDateTime.of(2024, 12, 6, 15, 30))
            .build();

    OrderDbo order2 =
        OrderDbo.builder()
            .contractDBO(dbContract2)
            .status(OrderStatus.Filled)
            .action(Types.Action.BUY)
            .totalQuantity(getBigDecimal(1))
            .avgFillPrice(getBigDecimal(14))
            .lastModified(LocalDateTime.of(2024, 12, 6, 15, 31))
            .build();
    OrderDbo order3 =
        OrderDbo.builder()
            .contractDBO(dbContract2)
            .status(OrderStatus.Filled)
            .action(Types.Action.BUY)
            .totalQuantity(getBigDecimal(2))
            .avgFillPrice(getBigDecimal(17))
            .lastModified(LocalDateTime.of(2024, 12, 6, 15, 32))
            .build();
    OrderDbo order4 =
        OrderDbo.builder()
            .contractDBO(dbContract1)
            .status(OrderStatus.Filled)
            .action(Types.Action.SELL)
            .totalQuantity(getBigDecimal(1))
            .avgFillPrice(getBigDecimal(15))
            .lastModified(LocalDateTime.of(2024, 12, 6, 15, 33))
            .build();
    OrderDbo order5 =
        OrderDbo.builder()
            .contractDBO(dbContract1)
            .status(OrderStatus.Filled)
            .action(Types.Action.BUY)
            .totalQuantity(getBigDecimal(1))
            .avgFillPrice(getBigDecimal(13))
            .lastModified(LocalDateTime.of(2024, 12, 6, 15, 34))
            .build();

    List<PositionDbo> instanceUnderTest =
        positionSplitService.splitGivenContractsFromPosition(
            List.of(order4, order1, order2, order3, order5), position);

    assertThat(instanceUnderTest)
        .containsExactlyInAnyOrder(
            PositionDbo.builder()
                .averageCost(getBigDecimal(13))
                .totalCost(getBigDecimal(13))
                .position(getBigDecimal(1))
                .contractDBO(order1.getContractDBO())
                .build(),
            PositionDbo.builder()
                .averageCost(getBigDecimal(16))
                .totalCost(getBigDecimal(48))
                .position(getBigDecimal(3))
                .contractDBO(order2.getContractDBO())
                .build());
    assertThat(instanceUnderTest.size()).isEqualTo(2);
    assertThat(
            instanceUnderTest.stream()
                .map(PositionDbo::getTotalCost)
                .reduce(BigDecimal.ZERO, BigDecimal::add))
        .isEqualTo(position.getTotalCost());
  }

  @Test
  void testSplit_withSellingPartiallyAndRebuyingOrders_first2Second3() {
    when(autoTradeIdService.setIdForAutoTrade(any())).thenReturn(null);
    ComboLegDbo positionLeg1 = createLeg(1, 5, Types.Action.BUY);
    ComboLegDbo positionLeg2 = createLeg(2, 5, Types.Action.BUY);
    ComboLegDbo positionLeg3 = createLeg(3, 2, Types.Action.SELL);
    ComboLegDbo positionLeg4 = createLeg(4, 2, Types.Action.SELL);
    ComboLegDbo contract1Leg1 = createLeg(1, 1, Types.Action.BUY);
    ComboLegDbo contract1Leg2 = createLeg(2, 1, Types.Action.BUY);
    ComboLegDbo contract1Leg3 = createLeg(3, 1, Types.Action.SELL);
    ComboLegDbo contract1Leg4 = createLeg(4, 1, Types.Action.SELL);
    ComboLegDbo contract2Leg1 = createLeg(1, 1, Types.Action.BUY);
    ComboLegDbo contract2Leg2 = createLeg(2, 1, Types.Action.BUY);

    List<ComboLegDbo> legs = List.of(positionLeg1, positionLeg2, positionLeg3, positionLeg4);
    List<ComboLegDbo> contract1Legs =
        List.of(contract1Leg1, contract1Leg2, contract1Leg3, contract1Leg4);
    List<ComboLegDbo> contract2Legs = List.of(contract2Leg1, contract2Leg2);
    ContractDbo positionContract = createBagContract(legs);
    PositionDbo position =
        PositionDbo.builder()
            .position(getBigDecimal(1))
            .totalCost(getBigDecimal(73))
            .averageCost(getBigDecimal(73))
            .contractDBO(positionContract)
            .build();
    ContractDbo dbContract1 = createBagContract(contract1Legs);
    ContractDbo dbContract2 = createBagContract(contract2Legs);

    OrderDbo order1 =
        OrderDbo.builder()
            .contractDBO(dbContract1)
            .status(OrderStatus.Filled)
            .action(Types.Action.BUY)
            .totalQuantity(getBigDecimal(2))
            .avgFillPrice(getBigDecimal(12))
            .lastModified(LocalDateTime.of(2024, 12, 6, 15, 30))
            .build();

    OrderDbo order2 =
        OrderDbo.builder()
            .contractDBO(dbContract2)
            .status(OrderStatus.Filled)
            .action(Types.Action.BUY)
            .totalQuantity(getBigDecimal(1))
            .avgFillPrice(getBigDecimal(14))
            .lastModified(LocalDateTime.of(2024, 12, 6, 15, 31))
            .build();
    OrderDbo order3 =
        OrderDbo.builder()
            .contractDBO(dbContract2)
            .status(OrderStatus.Filled)
            .action(Types.Action.BUY)
            .totalQuantity(getBigDecimal(2))
            .avgFillPrice(getBigDecimal(17))
            .lastModified(LocalDateTime.of(2024, 12, 6, 15, 32))
            .build();
    OrderDbo order4 =
        OrderDbo.builder()
            .contractDBO(dbContract1)
            .status(OrderStatus.Filled)
            .action(Types.Action.SELL)
            .totalQuantity(getBigDecimal(1))
            .avgFillPrice(getBigDecimal(15))
            .lastModified(LocalDateTime.of(2024, 12, 6, 15, 33))
            .build();
    OrderDbo order5 =
        OrderDbo.builder()
            .contractDBO(dbContract1)
            .status(OrderStatus.Filled)
            .action(Types.Action.BUY)
            .totalQuantity(getBigDecimal(1))
            .avgFillPrice(getBigDecimal(13))
            .lastModified(LocalDateTime.of(2024, 12, 6, 15, 34))
            .build();

    List<PositionDbo> instanceUnderTest =
        positionSplitService.splitGivenContractsFromPosition(
            List.of(order4, order1, order2, order3, order5), position);

    assertThat(instanceUnderTest)
        .containsExactlyInAnyOrder(
            PositionDbo.builder()
                .averageCost(getBigDecimal(12.5))
                .totalCost(getBigDecimal(25))
                .position(getBigDecimal(2))
                .contractDBO(order1.getContractDBO())
                .build(),
            PositionDbo.builder()
                .averageCost(getBigDecimal(16))
                .totalCost(getBigDecimal(48))
                .position(getBigDecimal(3))
                .contractDBO(order2.getContractDBO())
                .build());
    assertThat(instanceUnderTest.size()).isEqualTo(2);
    assertThat(
            instanceUnderTest.stream()
                .map(PositionDbo::getTotalCost)
                .reduce(BigDecimal.ZERO, BigDecimal::add))
        .isEqualTo(position.getTotalCost());
  }

  private ContractDbo createLegContract(int conId, Types.Right right, BigDecimal strike) {
    return ContractDbo.builder()
        .contractId(conId)
        .symbol(Symbol.SPX)
        .strike(strike)
        .right(right)
        .exchange("CBOE")
        .currency("USD")
        .lastTradeDate(TRADE_DATE)
        .securityType(Types.SecType.OPT)
        .build();
  }

  private ComboLegDbo createLeg(int conId, int ratio, Types.Action action) {
    return ComboLegDbo.builder()
        .ratio(ratio)
        .contractId(conId)
        .action(action)
        .exchange("CBOE")
        .build();
  }

  private ContractDbo createBagContract(List<ComboLegDbo> legs) {
    return ContractDbo.builder()
        .securityType(Types.SecType.BAG)
        .lastTradeDate(TRADE_DATE)
        .currency("USD")
        .exchange("CBOE")
        .symbol(Symbol.SPX)
        .tradingClass(Symbol.SPXW.name())
        .comboLegs(legs)
        .build();
  }
}
