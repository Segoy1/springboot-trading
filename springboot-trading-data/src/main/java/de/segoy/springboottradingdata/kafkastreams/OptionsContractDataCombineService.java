package de.segoy.springboottradingdata.kafkastreams;

import com.ib.client.Types;
import de.segoy.springboottradingdata.kafkastreams.util.RatioHelper;
import de.segoy.springboottradingdata.model.data.entity.ComboLegData;
import de.segoy.springboottradingdata.model.data.entity.ContractData;
import de.segoy.springboottradingdata.model.data.entity.PositionData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OptionsContractDataCombineService {

    private final RatioHelper ratioHelper;

    public PositionData combinePositions(PositionData receivedPosition, PositionData aggregatedPosition) {
        if (aggregatedPosition.getAccount().isEmpty()) {
            return receivedPosition;
        } else {
            ContractData aggContract = aggregatedPosition.getContractData();
            List<ComboLegData> comboLegs = aggContract.getComboLegs();
            if (receivedPosition.getContractData().getContractId().equals(aggContract.getContractId())) {
                return receivedPosition;
            } else {
                if (comboLegs.isEmpty()) {
                    comboLegs.add(
                            ComboLegData.builder()
                                    .contractId(aggContract.getContractId())
                                    .exchange(aggContract.getExchange())
                                    .action(sellOrBuy(aggregatedPosition.getPosition()))
                                    .ratio(1).build()
                    );
                    aggContract.setContractId(null);
                    aggContract.setComboLegsDescription(
                            aggContract.getTradingClass() + " " + aggContract.getLastTradeDate()
                                    + " \n" + aggContract.getStrike() + aggContract.getRight()
                                    + "/" + sellOrBuy(aggregatedPosition.getPosition()));
                }

                RatioHelper.Ratios ratios = ratioHelper.getRatio(
                        aggregatedPosition.getPosition().abs().intValue(),
                        receivedPosition.getPosition().abs().intValue());

                setRatiosForExisting(aggregatedPosition, ratios);

                ContractData receivedContract = receivedPosition.getContractData();
                comboLegs.add(
                        ComboLegData.builder()
                                .contractId(receivedContract.getContractId())
                                .exchange(receivedContract.getExchange())
                                .action(sellOrBuy(receivedPosition.getPosition()))
                                .ratio(ratios.received())
                                .build()
                );
                aggregatedPosition.getContractData().setComboLegsDescription(
                        aggregatedPosition.getContractData().getComboLegsDescription()
                                + "\n" + receivedContract.getStrike()
                                + receivedContract.getRight()
                                + "/" + sellOrBuy(receivedPosition.getPosition()));

                aggregatedPosition.setPosition(BigDecimal.valueOf(ratios.gcd()));

                //redundant maybe..
                aggContract.setComboLegs(comboLegs);
                aggregatedPosition.setContractData(aggContract);
                return aggregatedPosition;

            }
        }
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
