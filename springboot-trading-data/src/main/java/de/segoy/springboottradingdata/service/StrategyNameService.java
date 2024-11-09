package de.segoy.springboottradingdata.service;

import com.ib.client.Types;
import de.segoy.springboottradingdata.model.Leg;
import de.segoy.springboottradingdata.model.data.entity.ComboLegDbo;
import de.segoy.springboottradingdata.model.data.entity.ContractDbo;
import de.segoy.springboottradingdata.model.subtype.Strategy;
import de.segoy.springboottradingdata.repository.ContractRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StrategyNameService {

  private final ContractRepository contractRepository;

  public Strategy resolveStrategyFromComboLegs(final List<ComboLegDbo> legs) {
    List<Leg> legList = new ArrayList<>();
    legs.forEach(
        (leg) -> {
          ContractDbo legDetails =
              contractRepository.findAllByContractId(leg.getContractId()).get(0);
          legList.add(
              Leg.builder()
                  .action(leg.getAction())
                  .ratio(leg.getRatio())
                  .right(legDetails.getRight())
                  .strike(legDetails.getStrike().doubleValue())
                  .build());
        });
    return getStrategy(legList);
  }

  private Strategy getStrategy(final List<Leg> legs) {
    Map<Types.Action, List<Leg>> groupedByRight =
        legs.stream().collect(Collectors.groupingBy(Leg::getAction));
    List<Leg> buys = groupedByRight.get(Types.Action.BUY);
    List<Leg> sells = groupedByRight.get(Types.Action.SELL);
    if(buys.size()==2 && sells.size()==2) {
        // Find the smallest and largest strike in buys
        double minBuyStrike = buys.stream().mapToDouble(Leg::getStrike).min().getAsDouble();
        double maxBuyStrike = buys.stream().mapToDouble(Leg::getStrike).max().getAsDouble();

        // Find the smallest and largest strike in sells
        double minSellStrike = sells.stream().mapToDouble(Leg::getStrike).min().getAsDouble();
        double maxSellStrike = sells.stream().mapToDouble(Leg::getStrike).max().getAsDouble();

        if(minBuyStrike>minSellStrike && maxBuyStrike<maxSellStrike) {
            return Strategy.SHORT_IRON_CONDOR;
        }else if(minBuyStrike<minSellStrike && maxBuyStrike>maxSellStrike){
            return Strategy.IRON_CONDOR;
        }
    }else if(buys.size()==1 && sells.size()==1) {
        if(buys.get(0).getStrike()>sells.get(0).getStrike()) {
            return Strategy.BEAR_SPREAD;
        }else if(buys.get(0).getStrike()<sells.get(0).getStrike()) {
            return Strategy.BULL_SPREAD;
        }
    }else if((buys.size()==1&&sells.size()==2)||(buys.size()==2&&sells.size()==1)) {
        if(buys.size()==2 && sells.get(0).getRatio()==2){
            double sellStrike = sells.get(0).getStrike();
            double minBuyStrike = buys.stream().mapToDouble(Leg::getStrike).min().getAsDouble();
            double maxBuyStrike = buys.stream().mapToDouble(Leg::getStrike).max().getAsDouble();

            if(minBuyStrike<sellStrike && maxBuyStrike>sellStrike) {
                return Strategy.BUTTERFLY;
            }
        }
    }else if(buys.size()==2 && sells.isEmpty()) {
        return Strategy.STRANGLE;
    }else if(buys.isEmpty() && sells.size()==2) {
        return Strategy.SHORT_STRANGLE;
    }
    return Strategy.CUSTOM_STRATEGY;
  }
}
