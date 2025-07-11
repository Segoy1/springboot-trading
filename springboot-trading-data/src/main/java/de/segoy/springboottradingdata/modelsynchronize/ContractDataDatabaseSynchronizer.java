package de.segoy.springboottradingdata.modelsynchronize;

import com.ib.client.Contract;
import com.ib.client.Types;
import de.segoy.springboottradingdata.model.data.entity.ComboLegDbo;
import de.segoy.springboottradingdata.model.data.entity.ContractDbo;
import de.segoy.springboottradingdata.modelconverter.IBKRToContractDbo;
import de.segoy.springboottradingdata.repository.ComboLegRepository;
import de.segoy.springboottradingdata.repository.ContractRepository;
import de.segoy.springboottradingdata.service.ComboContractDataFinder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
public class ContractDataDatabaseSynchronizer {

  private final IBKRToContractDbo ibkrToContractDbo;
  private final ContractRepository contractRepository;
  private final ComboLegRepository comboLegRepository;
  private final ComboContractDataFinder comboContractDataFinder;

  public ContractDbo findInDBOrConvertAndSaveOrUpdateIfIdIsProvided(
      OptionalLong id, Contract contract) {
    boolean isCombo = Types.SecType.BAG.name().equals(contract.getSecType());
    if (!isCombo && contractRepository.findFirstByContractId(contract.conid()).isPresent()) {
      return contractRepository.findFirstByContractId(contract.conid()).get();
    } else if (isCombo) {
      if (!contract.comboLegs().isEmpty()) {
        List<String> contractIds =
            contract.comboLegs().stream().map(leg -> String.valueOf(leg.conid())).toList();
        List<ContractDbo> combos =
            contractRepository.findByComboLegsDescriptionContains(contractIds.get(0)).stream()
                .filter(
                    dbContract ->
                        contractIds.stream()
                            .allMatch(dbContract.getComboLegsDescription()::contains))
                .toList();
        if (combos.size() == 1) {
          return combos.get(0);
        }
        if (combos.size() > 1) {
          throw new IllegalStateException(
              "Contract with same ComboLegs must only exist once in DB!");
        }
      }
    }
    List<ComboLegDbo> comboLegs = new ArrayList<>();
    ContractDbo newContractDbo = ibkrToContractDbo.convertIBKRContract(contract);

    if (isCombo) {
      newContractDbo
          .getComboLegs()
          .forEach(
              (comboLeg) -> {
                comboLegs.add(saveComboLegIfNotExistent(comboLeg));
              });
      newContractDbo.setComboLegs(comboLegs);
      id = id.isPresent() ? id : comboContractDataFinder.findIdOfContractWithComboLegs(comboLegs);
    }
    id.ifPresent(newContractDbo::setId);
    return contractRepository.save(newContractDbo);
  }

  private ComboLegDbo saveComboLegIfNotExistent(ComboLegDbo comboLegDBO) {
    return comboLegRepository
        .findFirstByContractIdAndActionAndRatioAndExchange(
            comboLegDBO.getContractId(),
            comboLegDBO.getAction(),
            comboLegDBO.getRatio(),
            comboLegDBO.getExchange())
        .orElseGet(() -> comboLegRepository.save(comboLegDBO));
  }
}
