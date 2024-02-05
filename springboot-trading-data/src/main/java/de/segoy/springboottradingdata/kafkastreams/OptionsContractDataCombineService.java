package de.segoy.springboottradingdata.kafkastreams;

import com.ib.client.Types;
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

    /**
     * @Param PositionData receivedPosition is the
     *
     **/
    public PositionData combinePositions(PositionData receivedPosition, PositionData aggregatedPosition) {
        if (aggregatedPosition.getAccount() == null) {
            return receivedPosition;
        } else {
            ContractData aggContract = aggregatedPosition.getContractData();
            List<ComboLegData> comboLegs = aggContract.getComboLegs();
            if (receivedPosition.getContractData().getContractId().equals(aggContract.getContractId())) {
                return receivedPosition;
            } else {
                if (comboLegs.isEmpty()) {
                    comboLegs = new ArrayList<>();
                    comboLegs.add(
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
                    aggContract.setContractId(null);
                    aggContract.setRight(null);
                    aggContract.setExchange(null);
                    aggContract.setStrike(null);
                }

                //Calculating Ratios
                RatioHelper.Ratios ratios = ratioHelper.getRatio(
                        aggregatedPosition.getPosition().abs().intValue(),
                        receivedPosition.getPosition().abs().intValue());

                setRatiosForExisting(aggregatedPosition, ratios);

                //Sort out ComboLeg data
                setOrUpdateComboLegData(receivedPosition, comboLegs, ratios);

                //Set ComboLeg Description
                aggregatedPosition.getContractData()
                        .setComboLegsDescription(
                                aggregatedPosition.getContractData().getComboLegsDescription()
                                        + appendDescritpion(
                                        receivedPosition.getContractData().getStrike(),
                                        receivedPosition.getContractData().getRight(),
                                        receivedPosition.getPosition()));

                aggregatedPosition.setPosition(BigDecimal.valueOf(ratios.gcd()));
                aggContract.setSecurityType(Types.SecType.BAG);
                aggregatedPosition.setAverageCost(
                        getAverageCost(receivedPosition, aggregatedPosition));

                //redundant maybe..
                aggContract.setComboLegs(comboLegs);
                aggregatedPosition.setContractData(aggContract);
                return aggregatedPosition;

            }
        }
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

    private double getAverageCost(PositionData receivedPosition, PositionData aggregatedPosition) {
        double aggPos = 0;
        for (ComboLegData leg : aggregatedPosition.getContractData().getComboLegs()) {
            aggPos = aggPos + (leg.getRatio() * aggregatedPosition.getPosition().intValue());
        }
        double recPos = receivedPosition.getPosition().doubleValue();
        return (aggregatedPosition.getAverageCost() * aggPos + receivedPosition.getAverageCost() * recPos) / (aggPos + recPos);
    }


    private void setRatiosForExisting(PositionData aggregatedPosition, RatioHelper.Ratios ratios) {
        aggregatedPosition.getContractData().getComboLegs().forEach(
                (comboLeg) ->
                {
                    comboLeg.setRatio(comboLeg.getRatio() * ratios.aggregated());
                });
    }


    private Types.Action sellOrBuy(BigDecimal number) {
        return number.intValue() > 0 ? Types.Action.BUY : Types.Action.SELL;
    }
}
