package de.segoy.springboottradingdata.kafkastreams;

import com.ib.client.Types;
import de.segoy.springboottradingdata.kafkastreams.util.RatioHelper;
import de.segoy.springboottradingdata.model.data.kafka.ComboLegData;
import de.segoy.springboottradingdata.model.data.kafka.ContractData;
import de.segoy.springboottradingdata.model.data.kafka.PositionData;
import de.segoy.springboottradingdata.service.StrategyComboLegsDescriptionCreator;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
  public PositionData combinePositions(
      PositionData receivedPosition, PositionData aggregatedPosition) {
    if (aggregatedPosition.getAccount() == null) {
      return receivedPosition;
    } else {
      if (receivedPosition
          .getContractData()
          .getContractId()
          .equals(aggregatedPosition.getContractData().getContractId())) {
        return receivedPosition;

      } else {
        List<ComboLegData> updatedComboLegs = new ArrayList<>();

        ContractData aggContract = aggregatedPosition.getContractData();

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
        setNewContractId(receivedPosition.getContractData(), aggregatedPosition.getContractData());

        // Set updated ComboLegs
        aggContract.setComboLegs(updatedComboLegs);

        return aggregatedPosition;
      }
    }
  }

  private void setNewContractId(ContractData receivedContract, ContractData aggregatedContract) {
    aggregatedContract.setContractId(
        aggregatedContract.getContractId() + receivedContract.getContractId());
  }

  private void transformContractDataOnFirstAggregation(
      PositionData aggregatedPosition, List<ComboLegData> updatedComboLegs) {

    ContractData aggContract = aggregatedPosition.getContractData();
    updatedComboLegs.add(
        ComboLegData.builder()
            .contractId(aggContract.getContractId())
            .exchange(aggContract.getExchange())
            .action(sellOrBuy(aggregatedPosition.getPosition()))
            .ratio(1)
            .build());
    // Remove Values for single Option data
    aggContract.setRight(Types.Right.None);
    aggContract.setStrike(null);
    aggContract.setLocalSymbol(null);
    aggContract.setSecurityType(Types.SecType.BAG);
  }

  private void setOrUpdateComboLegDataAndCost(
      PositionData aggregatedPosition,
      PositionData receivedPosition,
      List<ComboLegData> comboLegs,
      RatioHelper.Ratios ratios) {
    ContractData receivedContract = receivedPosition.getContractData();
    Optional<ComboLegData> legOptional =
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
          aggregatedPosition.getTotalCost().add(receivedPosition.getTotalCost()));
      aggregatedPosition.setAverageCost(
          aggregatedPosition.getTotalCost().divide(aggregatedPosition.getPosition(), 2, RoundingMode.HALF_UP));
    }
    comboLegs.add(
        ComboLegData.builder()
            .contractId(receivedContract.getContractId())
            .exchange(receivedContract.getExchange())
            .action(sellOrBuy(receivedPosition.getPosition()))
            .ratio(ratios.received())
            .build());

    aggregatedPosition
        .getContractData()
        .setComboLegsDescription(
            strategyComboLegsDescriptionCreator.generateComboLegsDescription(
                StrategyComboLegsDescriptionCreator.StrategyDetails.builder()
                    .comboLegContractIds(
                        comboLegs.stream()
                            .map(ComboLegData::getContractId)
                            .collect(Collectors.toCollection(ArrayList::new)))
                    .lastTradeDate(aggregatedPosition.getContractData().getLastTradeDate())
                    .symbol(aggregatedPosition.getContractData().getSymbol())
                    .build()));
  }

  private void updateRatiosOnComboLegs(List<ComboLegData> legs, RatioHelper.Ratios ratios) {
    legs.forEach((comboLeg) -> comboLeg.setRatio(comboLeg.getRatio() * ratios.aggregated()));
  }

  private Types.Action sellOrBuy(BigDecimal number) {
    return number.intValue() > 0 ? Types.Action.BUY : Types.Action.SELL;
  }
}
