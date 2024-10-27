package de.segoy.springboottradingdata.kafkastreams;

import com.ib.client.Types;
import de.segoy.springboottradingdata.kafkastreams.util.RatioHelper;
import de.segoy.springboottradingdata.model.data.entity.ComboLegDbo;
import de.segoy.springboottradingdata.model.data.entity.ContractDbo;
import de.segoy.springboottradingdata.model.data.entity.PositionDbo;
import de.segoy.springboottradingdata.service.StrategyComboLegsDescriptionCreator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StreamOptionsContractDataCombineService {

  private final RatioHelper ratioHelper;
  private final StrategyComboLegsDescriptionCreator strategyComboLegsDescriptionCreator;

  /**
   * Aggregates all the Grouped Positions from the Kafka Topic into on Aggregated Position to handle
   * Option Strategies properly. Is fairly messy and will need more specification in the future.
   *
   * @param receivedPosition the Event fetched from the topic being processed.
   * @param aggregatedPosition the already aggregated and to be aggregated Combo Position
   */
  public PositionDbo combinePositions(
      PositionDbo receivedPosition, PositionDbo aggregatedPosition) {
    if (aggregatedPosition.getAccount() == null) {
      return receivedPosition;
    } else {
      if (receivedPosition
          .getContractDBO()
          .getContractId()
          .equals(aggregatedPosition.getContractDBO().getContractId())) {
        return receivedPosition;

      } else {
        List<ComboLegDbo> updatedComboLegs = new ArrayList<>();

        ContractDbo aggContract = aggregatedPosition.getContractDBO();

        if (aggContract.getComboLegs() == null || aggContract.getComboLegs().isEmpty()) {
          transformContractDataOnFirstAggregation(aggregatedPosition, updatedComboLegs);
        } else {
          updatedComboLegs.addAll(aggContract.getComboLegs());
          aggContract
              .getComboLegs()
              .forEach(
                  leg -> {
                    if (leg.getRatio().equals(0)) {
                      updatedComboLegs.remove(leg);
                    }
                  });
        }

        // Calculating Ratios only positive numbers
        RatioHelper.Ratios ratios =
            ratioHelper.getRatio(
                aggregatedPosition.getPosition().abs().intValue(),
                receivedPosition.getPosition().abs().intValue());

        updateRatiosOnComboLegs(updatedComboLegs, ratios);

        aggregatedPosition.setPosition(BigDecimal.valueOf(ratios.gcd()));

        // Sort out ComboLeg data and add to updated List
        setOrUpdateComboLegDataAndCost(
            aggregatedPosition, receivedPosition, updatedComboLegs, ratios);

        // Set Contract Id to added up Contract Data Ids to be unique
        setNewContractId(receivedPosition.getContractDBO(), aggregatedPosition.getContractDBO());

        // Set updated ComboLegs
        aggContract.setComboLegs(updatedComboLegs);

        return aggregatedPosition;
      }
    }
  }

  private void setNewContractId(ContractDbo receivedContract, ContractDbo aggregatedContract) {
    aggregatedContract.setContractId(
        aggregatedContract.getContractId() + receivedContract.getContractId());
  }

  private void transformContractDataOnFirstAggregation(
      PositionDbo aggregatedPosition, List<ComboLegDbo> updatedComboLegs) {

    ContractDbo aggContract = aggregatedPosition.getContractDBO();
    updatedComboLegs.add(
        ComboLegDbo.builder()
            .contractId(aggContract.getContractId())
            .exchange(aggContract.getExchange())
            .action(sellOrBuy(aggregatedPosition.getPosition()))
            .ratio(1)
            .build());
    // Remove Values for single Option data
    aggContract.setId(null);
    aggContract.setRight(Types.Right.None);
    aggContract.setStrike(null);
    aggContract.setSecurityType(Types.SecType.BAG);
  }

  private void setOrUpdateComboLegDataAndCost(
      PositionDbo aggregatedPosition,
      PositionDbo receivedPosition,
      List<ComboLegDbo> comboLegs,
      RatioHelper.Ratios ratios) {
    ContractDbo receivedContract = receivedPosition.getContractDBO();
    Optional<ComboLegDbo> legOptional =
        comboLegs.stream()
            .filter(
                (comboLegData) ->
                    comboLegData.getContractId().equals(receivedContract.getContractId()))
            .findFirst();
    if (legOptional.isPresent()) {
      comboLegs.remove(legOptional.get());
    } else {
      // Setting Costs and Position
      aggregatedPosition.setTotalCost(
          aggregatedPosition.getTotalCost() + receivedPosition.getTotalCost());
      aggregatedPosition.setAverageCost(
          aggregatedPosition.getTotalCost() / aggregatedPosition.getPosition().doubleValue());
    }
    comboLegs.add(
        ComboLegDbo.builder()
            .contractId(receivedContract.getContractId())
            .exchange(receivedContract.getExchange())
            .action(sellOrBuy(receivedPosition.getPosition()))
            .ratio(ratios.received())
            .build());

    aggregatedPosition
        .getContractDBO()
        .setComboLegsDescription(
            strategyComboLegsDescriptionCreator.generateComboLegsDescription(
                StrategyComboLegsDescriptionCreator.StrategyDetails.builder()
                    .comboLegs(comboLegs)
                    .lastTradeDate(aggregatedPosition.getContractDBO().getLastTradeDate())
                    .symbol(aggregatedPosition.getContractDBO().getSymbol())
                    .build()));
  }

  private void updateRatiosOnComboLegs(List<ComboLegDbo> legs, RatioHelper.Ratios ratios) {
    legs.forEach((comboLeg) -> comboLeg.setRatio(comboLeg.getRatio() * ratios.aggregated()));
  }

  private Types.Action sellOrBuy(BigDecimal number) {
    return number.intValue() > 0 ? Types.Action.BUY : Types.Action.SELL;
  }
}
