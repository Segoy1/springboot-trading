package de.segoy.springboottradingweb.spxautotrade.service;

import com.ib.client.Types;
import de.segoy.springboottradingdata.config.TradingConstants;
import de.segoy.springboottradingdata.model.data.entity.ComboLegDbo;
import de.segoy.springboottradingdata.model.data.entity.ContractDbo;
import de.segoy.springboottradingdata.repository.ContractRepository;
import de.segoy.springboottradingdata.service.StrategyComboLegsDescriptionCreator;
import de.segoy.springboottradingibkr.client.service.contract.UniqueContractDataProvider;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class StrategyStrikesUpdateService {
  private final ContractRepository contractRepository;
  private final UniqueContractDataProvider uniqueContractDataProvider;
  private final StrategyComboLegsDescriptionCreator comboLegsDescriptionCreator;

  @Transactional
  public ContractDbo updateStrategyStrikes(ContractDbo strategyContract) {
    List<ComboLegDbo> updatedLegs = new ArrayList<>();
    strategyContract
        .getComboLegs()
        .forEach(
            (leg) -> {
              contractRepository
                  .findFirstByContractId(leg.getContractId())
                  .ifPresent(
                      (legContract) -> {
                        updatedLegs.add(
                            ComboLegDbo.builder()
                                .action(leg.getAction())
                                .ratio(leg.getRatio())
                                .exchange(leg.getExchange())
                                .contractId(getNewLeg(legContract).getContractId())
                                .build());
                      });
            });
    strategyContract.setComboLegs(updatedLegs);
    strategyContract.setComboLegsDescription(
        comboLegsDescriptionCreator.generateComboLegsDescription(
            StrategyComboLegsDescriptionCreator.StrategyDetails.builder()
                .comboLegContractIds(
                    updatedLegs.stream()
                        .map(ComboLegDbo::getContractId)
                        .collect(Collectors.toCollection(ArrayList::new)))
                .lastTradeDate(strategyContract.getLastTradeDate())
                .symbol(strategyContract.getSymbol())
                .build()));
    log.info("Update Strategy Legs: {}", strategyContract.getComboLegs());
    return contractRepository.save(strategyContract);
  }

  private ContractDbo getNewLeg(ContractDbo legContract) {
    BigDecimal updatedStrike;
    if (legContract.getRight().equals(Types.Right.Call)) {
      updatedStrike =
          legContract.getStrike().add(BigDecimal.valueOf(legContract.getSymbol().optionInterval()));
    } else if (legContract.getRight().equals(Types.Right.Put)) {
      updatedStrike =
          legContract
              .getStrike()
              .subtract(BigDecimal.valueOf(legContract.getSymbol().optionInterval()));
    } else {
      throw new IllegalStateException("Strike Price in Strategy not set. This must not happen!");
    }
    return uniqueContractDataProvider
        .getExistingContractDataOrCallApi(
            ContractDbo.builder()
                .lastTradeDate(legContract.getLastTradeDate())
                .strike(updatedStrike)
                .securityType(Types.SecType.OPT)
                .currency(TradingConstants.USD)
                .symbol(legContract.getSymbol())
                .right(legContract.getRight())
                .build())
        .orElseThrow();
  }
}
