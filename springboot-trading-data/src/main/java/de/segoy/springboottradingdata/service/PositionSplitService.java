package de.segoy.springboottradingdata.service;

import com.ib.client.Types;
import de.segoy.springboottradingdata.model.data.entity.ComboLegDbo;
import de.segoy.springboottradingdata.model.data.entity.ContractDbo;
import de.segoy.springboottradingdata.model.data.entity.OrderDbo;
import de.segoy.springboottradingdata.model.data.entity.PositionDbo;
import de.segoy.springboottradingdata.modelsynchronize.PositionDataDatabaseSynchronizer;
import de.segoy.springboottradingdata.optionstradingservice.AutoTradeIdService;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PositionSplitService {

  private final StrategyComboLegsDescriptionCreator strategyComboLegsDescriptionCreator;
  private final AutoTradeIdService autoTradeIdService;
  private final PositionDataDatabaseSynchronizer positionDataDatabaseSynchronizer;

  // Maybe here will be an issue with some jdbc combolegs stuff.. will see
  @Transactional
  public List<PositionDbo> splitGivenContractsFromPosition(
      List<OrderDbo> filledOrders, PositionDbo accumulatedPosition) {
    Map<ContractDbo, PositionDbo> updatedPositions = new HashMap<>();
    Map<Integer, Integer> newAccumulatedPositionLegs =
        getLegsWithActualAmount(
            accumulatedPosition.getContractDBO().getComboLegs(),
            accumulatedPosition.getPosition().intValue());

    for (OrderDbo order :
        filledOrders.stream().sorted(Comparator.comparing(OrderDbo::getLastModified)).toList()) {
      organizePosition(order, updatedPositions, newAccumulatedPositionLegs);
    }
    // If there is anything leftover we aggregate it into a new position here
    List<PositionDbo> positions = new ArrayList<>(updatedPositions.values().stream().toList());
    newAccumulatedPositionLegs = removeZeroPositionsFromMap(newAccumulatedPositionLegs);
    if (!newAccumulatedPositionLegs.isEmpty()) {
      positions.add(
          getLeftOverPositionAndSaveIt(accumulatedPosition, newAccumulatedPositionLegs, positions));
    }
    return positions.stream()
        .map(position -> positionDataDatabaseSynchronizer.updateInDbOrSave(position).orElseThrow())
        .toList();
  }

  private PositionDbo getLeftOverPositionAndSaveIt(
      PositionDbo accumulatedPosition,
      Map<Integer, Integer> newAccumulatedPositionLegs,
      List<PositionDbo> positions) {
    PositionDbo leftovers =
        aggregateLeftOverPosition(newAccumulatedPositionLegs, accumulatedPosition);
    leftovers.setTotalCost(
        getDifferenceInTotal(accumulatedPosition, positions).setScale(2, RoundingMode.HALF_UP));
    leftovers.setAverageCost(
        leftovers.getTotalCost().divide(leftovers.getPosition(), 2, RoundingMode.HALF_UP));
    return leftovers;
  }

  private BigDecimal getDifferenceInTotal(
      PositionDbo accumulatedPosition, List<PositionDbo> positions) {
    return accumulatedPosition
        .getTotalCost()
        .subtract(
            positions.stream()
                .map(PositionDbo::getTotalCost)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
  }

  private void organizePosition(
      OrderDbo order,
      Map<ContractDbo, PositionDbo> updatedPositions,
      Map<Integer, Integer> newAccumulatedPositionLegs) {
    BigDecimal total =
        order
            .getAvgFillPrice()
            .multiply(order.getTotalQuantity())
            .setScale(2, RoundingMode.HALF_UP);

    ContractDbo orderContract = order.getContractDBO();
    if (updatedPositions.get(orderContract) != null) {
      PositionDbo existingPos = updatedPositions.get(orderContract);
      if (order.getAction().equals(Types.Action.SELL)) {
        existingPos.setPosition(existingPos.getPosition().subtract(order.getTotalQuantity()));
        existingPos.setTotalCost(
            existingPos
                .getTotalCost()
                .subtract(existingPos.getAverageCost().multiply(order.getTotalQuantity())));
      } else {
        existingPos.setPosition(existingPos.getPosition().add(order.getTotalQuantity()));
        existingPos.setTotalCost(
            existingPos.getTotalCost().add(total).setScale(2, RoundingMode.HALF_UP));
        existingPos.setAverageCost(
            existingPos.getTotalCost().divide(existingPos.getPosition(), 2, RoundingMode.HALF_UP));
      }
      updatedPositions.put(existingPos.getContractDBO(), existingPos);
    } else {
      if (order.getAction().equals(Types.Action.SELL)) {
        throw new IllegalStateException(
            "Short Selling a strategy should never happen as first Order");
      } else {
        updatedPositions.put(
            orderContract,
            PositionDbo.builder()
                .id(autoTradeIdService.setIdForAutoTrade(orderContract))
                .contractDBO(orderContract)
                .position(order.getTotalQuantity())
                .totalCost(total)
                .averageCost(order.getAvgFillPrice())
                .build());
      }
    }

    orderContract
        .getComboLegs()
        .forEach(
            (leg) -> {
              int changeAmount = leg.getRatio() * order.getTotalQuantity().intValue();
              if (leg.getAction().equals(Types.Action.SELL)) {
                changeAmount = changeAmount * -1;
              }
              if (order.getAction().equals(Types.Action.SELL)) {
                changeAmount = changeAmount * -1;
              }
              newAccumulatedPositionLegs.put(
                  leg.getContractId(),
                  newAccumulatedPositionLegs.get(leg.getContractId()) - changeAmount);
            });
  }

  private Map<Integer, Integer> getLegsWithActualAmount(List<ComboLegDbo> legs, int position) {
    return legs.stream()
        .collect(
            Collectors.toMap(
                ComboLegDbo::getContractId,
                leg -> leg.getRatio() * shortLongFactor(leg.getAction()) * position));
  }

  private int shortLongFactor(Types.Action action) {
    if (action.equals(Types.Action.SELL)) return -1;
    else return 1;
  }

  private PositionDbo aggregateLeftOverPosition(
      Map<Integer, Integer> reducedPositionLegs, PositionDbo accumulatedPosition) {
    ContractDbo contract = accumulatedPosition.getContractDBO();
    int position = getGcdThatAlsoServesAsPosition(reducedPositionLegs);
    List<ComboLegDbo> comboLegs =
        accumulatedPosition.getContractDBO().getComboLegs().stream()
            .filter(leg -> reducedPositionLegs.containsKey(leg.getContractId()))
            .peek(
                leg -> {
                  int ratio = reducedPositionLegs.get(leg.getContractId()) / position;
                  leg.setRatio(Math.abs(ratio));
                  if (ratio > 0) {
                    leg.setAction(Types.Action.BUY);
                  } else {
                    leg.setAction(Types.Action.SELL);
                  }
                })
            .toList();

    contract.setId(null);
    contract.setComboLegs(comboLegs);
    contract.setComboLegsDescription(
        strategyComboLegsDescriptionCreator.generateComboLegsDescription(
            StrategyComboLegsDescriptionCreator.StrategyDetails.builder()
                .symbol(contract.getSymbol())
                .lastTradeDate(contract.getLastTradeDate())
                .comboLegContractIds(reducedPositionLegs.keySet().stream().toList())
                .build()));

    return PositionDbo.builder()
        .position(BigDecimal.valueOf(position).setScale(2, RoundingMode.HALF_UP))
        .contractDBO(contract)
        .account(accumulatedPosition.getAccount())
        .build();
  }

  private Map<Integer, Integer> removeZeroPositionsFromMap(Map<Integer, Integer> positionLegs) {
    return positionLegs.entrySet().stream()
        .filter(entry -> entry.getValue() != 0)
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
  }

  private int getGcdThatAlsoServesAsPosition(Map<Integer, Integer> positionLegs) {
    return positionLegs.values().stream()
        .map(BigInteger::valueOf)
        .reduce(BigInteger::gcd)
        .orElseThrow()
        .abs()
        .intValue();
  }
}
