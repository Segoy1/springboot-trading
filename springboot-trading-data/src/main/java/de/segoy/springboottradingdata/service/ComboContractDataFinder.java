package de.segoy.springboottradingdata.service;

import de.segoy.springboottradingdata.model.data.entity.ComboLegDbo;
import de.segoy.springboottradingdata.model.data.entity.ContractDbo;
import de.segoy.springboottradingdata.repository.ContractRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalLong;

@Service
@RequiredArgsConstructor
public class ComboContractDataFinder {

  private final ContractRepository contractRepository;

  public OptionalLong findIdOfContractWithComboLegs(List<ComboLegDbo> comboLegs) {
    return findContractWithComboLegs(comboLegs).map(ContractDbo::getId).map(OptionalLong::of).orElseGet(OptionalLong::empty);
  }

  public Optional<ContractDbo> findContractWithComboLegs(List<ComboLegDbo> comboLegs) {
    final List<ContractDbo> oldContracts = new ArrayList<>();
    for (ComboLegDbo comboLeg : comboLegs) {
        List<ContractDbo> contracts = new ArrayList<>(contractRepository
                .findByComboLegsDescriptionContains(comboLeg.getContractId().toString()));
      if (contracts.isEmpty()) {
        // if none are found on any Given ComboLeg there is no existing Contract
        return Optional.empty();
      } else if (oldContracts.isEmpty()) {
        // populate List of existing Contracts with first iteration
        oldContracts.addAll(contracts);
      } else {
        // remove all existing Contracts that are not in latest iteration
        oldContracts.removeIf(contract -> !contracts.contains(contract));

        if (oldContracts.isEmpty()) {
          return Optional.empty();
        }
      }
    }
    // if list is not empty return first value, there should never be more than one
    return Optional.ofNullable(oldContracts.get(0));
  }
}
