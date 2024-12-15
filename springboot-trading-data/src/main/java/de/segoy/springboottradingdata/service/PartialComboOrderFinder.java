package de.segoy.springboottradingdata.service;

import com.ib.client.OrderStatus;
import de.segoy.springboottradingdata.model.data.entity.ComboLegDbo;
import de.segoy.springboottradingdata.model.data.entity.ContractDbo;
import de.segoy.springboottradingdata.model.data.entity.OrderDbo;
import de.segoy.springboottradingdata.repository.ContractRepository;
import de.segoy.springboottradingdata.repository.OrderRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PartialComboOrderFinder {

  private final ContractRepository contractRepository;
  private final OrderRepository orderRepository;

  public List<OrderDbo> findExistingStrategyContractsInCombo(ContractDbo contract) {
    List<ContractDbo> strategyContracts = getContracts(contract);
    return strategyContracts.stream()
        .flatMap(
            dbContract ->
                orderRepository.findByContractDBO(dbContract).stream()
                    .filter(order -> order.getStatus().equals(OrderStatus.Filled)))
        .toList();
  }

  private List<ContractDbo> getContracts(ContractDbo contract) {
    List<ContractDbo> strategyContracts = new ArrayList<>();
    Set<Integer> ids =
        contract.getComboLegs().stream()
            .map(ComboLegDbo::getContractId)
            .collect(Collectors.toSet());
    for (Integer id : ids) {
      List<ContractDbo> strategies =
          contractRepository.findByComboLegsDescriptionContains(String.valueOf(id));
      if (!strategies.isEmpty()) {
        for (ContractDbo strategy : strategies) {
          Set<Integer> strategyIds =
              strategy.getComboLegs().stream()
                  .map(ComboLegDbo::getContractId)
                  .collect(Collectors.toSet());
          if (ids.containsAll(strategyIds) && !strategyContracts.contains(strategy)) {
            strategyContracts.add(strategy);
          }
        }
      }
    }
    return strategyContracts;
  }
}
