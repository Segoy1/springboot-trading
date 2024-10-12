package de.segoy.springboottradingdata.kafkastreams;

import com.ib.client.Types;
import de.segoy.springboottradingdata.kafkastreams.util.RatioHelper;
import de.segoy.springboottradingdata.model.data.entity.ComboLegDataDBO;
import de.segoy.springboottradingdata.model.data.entity.ContractDataDBO;
import de.segoy.springboottradingdata.model.data.entity.PositionDataDBO;
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

    /**
     * Aggregates all the Grouped Positions from the Kafka Topic into on Aggregated Position to handle Option
     * Strategies properly. Is fairly messy and will need more specification in the future.
     *
     * @param receivedPosition   the Event fetched from the topic being processed.
     * @param aggregatedPosition the already aggregated and to be aggregated Combo Position
     **/
    public PositionDataDBO combinePositions(PositionDataDBO receivedPosition, PositionDataDBO aggregatedPosition) {
        if (aggregatedPosition.getAccount() == null) {
            return receivedPosition;
        } else {
            if (receivedPosition.getContractDataDBO().getContractId()
                    .equals(aggregatedPosition.getContractDataDBO().getContractId())) {
                return receivedPosition;


            } else {
                List<ComboLegDataDBO> updatedComboLegs = new ArrayList<>();

                ContractDataDBO aggContract = aggregatedPosition.getContractDataDBO();

                if (aggContract.getComboLegs() == null || aggContract.getComboLegs().isEmpty()) {
                    transformContractDataOnFirstAggregation(aggregatedPosition, updatedComboLegs);
                } else {
                    updatedComboLegs.addAll(aggContract.getComboLegs());
                    aggContract.getComboLegs().forEach(leg -> {
                        if (leg.getRatio().equals(0)) {
                            updatedComboLegs.remove(leg);
                        }
                    });
                }

                //Calculating Ratios only positive numbers
                RatioHelper.Ratios ratios = ratioHelper.getRatio(
                        aggregatedPosition.getPosition().abs().intValue(),
                        receivedPosition.getPosition().abs().intValue());


                updateRatiosOnComboLegs(updatedComboLegs, ratios);

                aggregatedPosition.setPosition(BigDecimal.valueOf(ratios.gcd()));

                //Sort out ComboLeg data and add to updated List
                setOrUpdateComboLegDataAndCost(aggregatedPosition, receivedPosition, updatedComboLegs, ratios);

                //Set Contract Id to added up Contract Data Ids to be unique
                setNewContractId(receivedPosition.getContractDataDBO(), aggregatedPosition.getContractDataDBO());

                //Set updated ComboLegs
                aggContract.setComboLegs(updatedComboLegs);

                return aggregatedPosition;

            }
        }
    }

    private void setNewContractId(ContractDataDBO receivedContract, ContractDataDBO aggregatedContract) {
        aggregatedContract.setContractId(aggregatedContract.getContractId()+ receivedContract.getContractId());
    }

    private void transformContractDataOnFirstAggregation(PositionDataDBO aggregatedPosition,
                                                         List<ComboLegDataDBO> updatedComboLegs) {

        ContractDataDBO aggContract = aggregatedPosition.getContractDataDBO();
        updatedComboLegs.add(
                ComboLegDataDBO.builder()
                        .contractId(aggContract.getContractId())
                        .exchange(aggContract.getExchange())
                        .action(sellOrBuy(aggregatedPosition.getPosition()))
                        .ratio(1).build()
        );
        //Remove Values for single Option data
        aggContract.setId(null);
        aggContract.setRight(Types.Right.None);
        aggContract.setStrike(null);
        aggContract.setSecurityType(Types.SecType.BAG);

    }

    private void setOrUpdateComboLegDataAndCost(PositionDataDBO aggregatedPosition, PositionDataDBO receivedPosition,
                                                List<ComboLegDataDBO> comboLegs,
                                                RatioHelper.Ratios ratios) {
        ContractDataDBO receivedContract = receivedPosition.getContractDataDBO();
        Optional<ComboLegDataDBO> legOptional = comboLegs.stream().filter(
                        (comboLegData) ->
                                comboLegData.getContractId().equals(receivedContract.getContractId()))
                .findFirst();
        if (legOptional.isPresent()) {
            comboLegs.remove(legOptional.get());
        } else {
            //Setting Costs and Position
            aggregatedPosition.setTotalCost(aggregatedPosition.getTotalCost() + receivedPosition.getTotalCost());
            aggregatedPosition.setAverageCost(
                    aggregatedPosition.getTotalCost() / aggregatedPosition.getPosition().doubleValue());

        }
        comboLegs.add(
                ComboLegDataDBO.builder()
                        .contractId(receivedContract.getContractId())
                        .exchange(receivedContract.getExchange())
                        .action(sellOrBuy(receivedPosition.getPosition()))
                        .ratio(ratios.received())
                        .build()
        );
        aggregatedPosition.getContractDataDBO().setComboLegsDescription(generateComboLegDescription(comboLegs));
    }

    private static String generateComboLegDescription(List<ComboLegDataDBO> comboLegs) {
        StringBuilder description = new StringBuilder();
        comboLegs.forEach(leg -> {
            description.append(leg.getContractId()).append(", ").append(leg.getRatio()).append(" | ");
        });
        return description.toString();
    }

    private void updateRatiosOnComboLegs(List<ComboLegDataDBO> legs, RatioHelper.Ratios ratios) {
        legs.forEach(
                (comboLeg) ->
                        comboLeg.setRatio(comboLeg.getRatio() * ratios.aggregated()));
    }


    private Types.Action sellOrBuy(BigDecimal number) {
        return number.intValue() > 0 ? Types.Action.BUY : Types.Action.SELL;
    }
}
