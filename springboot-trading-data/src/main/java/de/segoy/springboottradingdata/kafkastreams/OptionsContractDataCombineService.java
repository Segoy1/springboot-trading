package de.segoy.springboottradingdata.kafkastreams;

import com.ib.client.Types;
import de.segoy.springboottradingdata.config.PropertiesConfig;
import de.segoy.springboottradingdata.kafkastreams.util.RatioHelper;
import de.segoy.springboottradingdata.model.data.entity.ComboLegData;
import de.segoy.springboottradingdata.model.data.entity.ContractData;
import de.segoy.springboottradingdata.model.data.entity.PositionData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OptionsContractDataCombineService {

    private final RatioHelper ratioHelper;
    private final PropertiesConfig propertiesConfig;

    /**
     * @Param PositionData receivedPosition is the
     **/
    public PositionData combinePositions(PositionData receivedPosition, PositionData aggregatedPosition) {
        if (aggregatedPosition.getAccount() == null) {
            return receivedPosition;
        } else {
            if (receivedPosition.getContractData().getContractId()
                    .equals(aggregatedPosition.getContractData().getContractId())) {
                return receivedPosition;
            } else {
                List<ComboLegData> updatedComboLegs = new ArrayList<>();

                ContractData aggContract = aggregatedPosition.getContractData();

                if (aggContract.getComboLegs() == null || aggContract.getComboLegs().isEmpty()) {
                    transformContractDataOnFirstAggregation(aggregatedPosition, updatedComboLegs);
                } else {
                    updatedComboLegs.addAll(aggContract.getComboLegs());
                }

                //Calculating Ratios
                RatioHelper.Ratios ratios = ratioHelper.getRatio(
                        aggregatedPosition.getPosition().intValue(),
                        receivedPosition.getPosition().intValue());


                updateRatiosOnComboLegs(updatedComboLegs, ratios);

                //Sort out ComboLeg data and add to updated List
                setOrUpdateComboLegData(receivedPosition, updatedComboLegs, ratios);

                //Set updated ComboLegs
                aggContract.setComboLegs(updatedComboLegs);

                //Set ComboLeg Description
                aggContract.setComboLegsDescription(
                        aggregatedPosition.getContractData().getComboLegsDescription()
                                + appendDescritpion(
                                receivedPosition.getContractData().getStrike(),
                                receivedPosition.getContractData().getRight(),
                                receivedPosition.getPosition()));


                //Setting Costs and Position
                aggregatedPosition.setTotalCost(aggregatedPosition.getTotalCost() + receivedPosition.getTotalCost());
                aggregatedPosition.setPosition(BigDecimal.valueOf(ratios.gcd()));
                aggregatedPosition.setAverageCost(
                        aggregatedPosition.getTotalCost() / aggregatedPosition.getPosition().doubleValue());


                return aggregatedPosition;

            }
        }
    }

    private void transformContractDataOnFirstAggregation(PositionData aggregatedPosition,
                                                         List<ComboLegData> updatedComboLegs) {

        ContractData aggContract = aggregatedPosition.getContractData();
        updatedComboLegs.add(
                ComboLegData.builder()
                        .contractId(aggContract.getContractId())
                        .exchange(aggContract.getExchange())
                        .action(sellOrBuy(aggregatedPosition.getPosition()))
                        .ratio(1).build()
        );
        //Remove Values for single Option data
        aggContract.setComboLegsDescription(
                aggContract.getTradingClass() + " " + aggContract.getLastTradeDate()
                        + appendDescritpion(aggContract.getStrike(), aggContract.getRight(),
                        aggregatedPosition.getPosition()));
        aggContract.setContractId(propertiesConfig.getCOMBO_CONTRACT_ID());
        aggContract.setRight(null);
        aggContract.setExchange(null);
        aggContract.setStrike(null);
        aggContract.setSecurityType(Types.SecType.BAG);

    }

    private void setOrUpdateComboLegData(PositionData receivedPosition, List<ComboLegData> comboLegs,
                                         RatioHelper.Ratios ratios) {
        ContractData receivedContract = receivedPosition.getContractData();
        comboLegs.stream().filter(
                        (comboLegData) ->
                                comboLegData.getContractId().equals(receivedContract.getContractId()))
                .findFirst().ifPresentOrElse(
                        comboLegData -> {
                            comboLegData.setRatio(ratios.received());
                            comboLegData.setAction(sellOrBuy(receivedPosition.getPosition()));
                        }, () -> {
                            comboLegs.add(
                                    ComboLegData.builder()
                                            .contractId(receivedContract.getContractId())
                                            .exchange(receivedContract.getExchange())
                                            .action(sellOrBuy(receivedPosition.getPosition()))
                                            .ratio(ratios.received())
                                            .build()
                            );
                        });
    }

    private String appendDescritpion(BigDecimal strike, Types.Right right, BigDecimal position) {
        return "\n" + strike + right + "/" + sellOrBuy(position);
    }

    private void updateRatiosOnComboLegs(List<ComboLegData> legs, RatioHelper.Ratios ratios) {
        legs.forEach(
                (comboLeg) ->
                        comboLeg.setRatio(comboLeg.getRatio() * ratios.aggregated()));
    }


    private Types.Action sellOrBuy(BigDecimal number) {
        return number.intValue() > 0 ? Types.Action.BUY : Types.Action.SELL;
    }
}
