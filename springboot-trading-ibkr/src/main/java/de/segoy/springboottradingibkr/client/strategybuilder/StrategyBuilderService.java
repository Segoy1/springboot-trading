package de.segoy.springboottradingibkr.client.strategybuilder;

import com.ib.client.Types;
import de.segoy.springboottradingdata.constants.AutoDayTradeConstants;
import de.segoy.springboottradingdata.model.Leg;
import de.segoy.springboottradingdata.model.data.StrategyContractData;
import de.segoy.springboottradingdata.model.data.entity.ComboLegDbo;
import de.segoy.springboottradingdata.model.data.entity.ContractDbo;
import de.segoy.springboottradingibkr.client.service.contract.UniqueContractDataProvider;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.util.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StrategyBuilderService {

  private final UniqueContractDataProvider uniqueContractDataProvider;

  @Transactional
  public Optional<ContractDbo> getComboLegContractData(StrategyContractData strategyContractData) {
    ContractDbo contractDBO = strategyContractData.getContractDBO();
    try {
      contractDBO.setComboLegs(legListBuilder(contractDBO, strategyContractData.getStrategyLegs()));
      setComboLegsDescription(contractDBO);
      return uniqueContractDataProvider.getExistingContractDataOrCallApi(contractDBO);
    } catch (NoSuchElementException e) {
      return Optional.empty();
    }
  }

  private void setComboLegsDescription(ContractDbo contractDBO) {
    StringBuilder description = new StringBuilder();
    description
        .append(contractDBO.getLastTradeDate())
        .append(AutoDayTradeConstants.DELIMITER)
        .append(contractDBO.getSymbol().name())
        .append(" | ");
    for (ComboLegDbo leg : contractDBO.getComboLegs()) {
      description.append(leg.getContractId()).append(" | ");
    }
    contractDBO.setComboLegsDescription(description.toString());
  }

  private List<ComboLegDbo> legListBuilder(ContractDbo contractDBO, List<Leg> legs) {
    List<ComboLegDbo> legData = new ArrayList<>();

    legs.forEach(
        (leg) -> {
          ContractDbo legContract =
              uniqueContractDataProvider
                  .getExistingContractDataOrCallApi(singleLegBuilder(contractDBO, leg))
                  .orElseThrow();
          legData.add(buildComboLegData(legContract, leg));
        });
    return legData;
  }

  private ComboLegDbo buildComboLegData(ContractDbo contractDboBuyPut, Leg leg) {
    return ComboLegDbo.builder()
        .contractId(contractDboBuyPut.getContractId())
        .ratio(leg.getRatio())
        .action(leg.getAction())
        .exchange(contractDboBuyPut.getExchange())
        .build();
  }

  private ContractDbo singleLegBuilder(ContractDbo contractDBO, Leg leg) {
    return ContractDbo.builder()
        .symbol(contractDBO.getSymbol())
        .securityType(Types.SecType.OPT)
        .currency(contractDBO.getCurrency())
        .exchange(contractDBO.getExchange())
        .tradingClass(contractDBO.getTradingClass())
        .strike(BigDecimal.valueOf(leg.getStrike()))
        .right(leg.getRight())
        .lastTradeDate(contractDBO.getLastTradeDate())
        .build();
  }
}
