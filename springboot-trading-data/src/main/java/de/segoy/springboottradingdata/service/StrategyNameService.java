package de.segoy.springboottradingdata.service;

import com.ib.client.Types;
import de.segoy.springboottradingdata.model.Leg;
import de.segoy.springboottradingdata.model.data.entity.ComboLegDbo;
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
          contractRepository
              .findFirstByContractId(leg.getContractId())
              .ifPresent(
                  (legDetails) ->
                      legList.add(
                          Leg.builder()
                              .action(leg.getAction())
                              .ratio(leg.getRatio())
                              .right(legDetails.getRight())
                              .strike(legDetails.getStrike().doubleValue())
                              .build()));
        });
    return getStrategy(legList);
  }

  private Strategy getStrategy(final List<Leg> legs) {
    Map<Types.Action, List<Leg>> groupedByAction =
        legs.stream().collect(Collectors.groupingBy(Leg::getAction));
    List<Leg> buys = groupedByAction.get(Types.Action.BUY);
    List<Leg> sells = groupedByAction.get(Types.Action.SELL);
    if (buys.stream().allMatch(buy -> buy.getRatio().equals(buys.get(0).getRatio()))
        && sells.stream().allMatch(sell -> sell.getRatio().equals(sells.get(0).getRatio()))) {
      if (buys.size() == 2 && sells.size() == 2) {
        // Find the smallest and largest strike in buys
        double minBuyStrike = buys.stream().mapToDouble(Leg::getStrike).min().getAsDouble();
        double maxBuyStrike = buys.stream().mapToDouble(Leg::getStrike).max().getAsDouble();

        // Find the smallest and largest strike in sells
        double minSellStrike = sells.stream().mapToDouble(Leg::getStrike).min().getAsDouble();
        double maxSellStrike = sells.stream().mapToDouble(Leg::getStrike).max().getAsDouble();

        if (minBuyStrike > minSellStrike && maxBuyStrike < maxSellStrike) {
          if (maxBuyStrike == minBuyStrike) {
            return Strategy.SHORT_IRON_BUTTERFLY;
          }
          return Strategy.SHORT_IRON_CONDOR;
        } else if (minBuyStrike < minSellStrike && maxBuyStrike > maxSellStrike) {
          if (maxSellStrike == minSellStrike) {
            return Strategy.IRON_BUTTERFLY;
          }
          return Strategy.IRON_CONDOR;
        }
      } else if (buys.size() == 1
          && sells.size() == 1
          && buys.get(0).getRight().equals(sells.get(0).getRight())) {
        if (buys.get(0).getStrike() > sells.get(0).getStrike()) {
          return Strategy.BEAR_SPREAD;
        } else if (buys.get(0).getStrike() < sells.get(0).getStrike()) {
          return Strategy.BULL_SPREAD;
        }
      } else if (buys.size() == 2 && sells.isEmpty() && !buys.get(0).getRight().equals(buys.get(1).getRight())) {
        return Strategy.STRANGLE;
      } else if (buys.isEmpty() && sells.size() == 2 && !sells.get(0).getRight().equals(sells.get(1).getRight())) {
        return Strategy.SHORT_STRANGLE;
      }
    } else if ((buys.size() == 1 && sells.size() == 2) || (buys.size() == 2 && sells.size() == 1)) {
      if (buys.size() == 2 && sells.get(0).getRatio() % 2 == 0) {
        double sellStrike = sells.get(0).getStrike();
        double minBuyStrike = buys.stream().mapToDouble(Leg::getStrike).min().getAsDouble();
        double maxBuyStrike = buys.stream().mapToDouble(Leg::getStrike).max().getAsDouble();

        if (minBuyStrike < sellStrike && maxBuyStrike > sellStrike) {
          return Strategy.BUTTERFLY;
        }
      }
    }
    return Strategy.CUSTOM_STRATEGY;
  }
}
