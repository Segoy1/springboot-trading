package de.segoy.springboottradingdata.modelconverter;

import de.segoy.springboottradingdata.model.data.entity.ComboLegDbo;
import de.segoy.springboottradingdata.model.data.entity.ContractDbo;
import de.segoy.springboottradingdata.model.data.kafka.ComboLegData;
import de.segoy.springboottradingdata.model.data.kafka.ContractData;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class ContractDataToDbo {

  public ContractDbo convert(ContractData contractData) {
    return ContractDbo.builder()
        .contractId(contractData.getContractId())
        .symbol(contractData.getSymbol())
        .securityType(contractData.getSecurityType())
        .currency(contractData.getCurrency())
        .exchange(contractData.getExchange())
        .lastTradeDate(contractData.getLastTradeDate())
        .strike(contractData.getStrike())
        .right(contractData.getRight())
        .multiplier(contractData.getMultiplier())
        .localSymbol(contractData.getLocalSymbol())
        .tradingClass(contractData.getTradingClass())
        .includeExpired(contractData.isIncludeExpired())
        .comboLegsDescription(contractData.getComboLegsDescription())
        .comboLegs(convertLegs(contractData.getComboLegs()))
        .build();
  }

  private List<ComboLegDbo> convertLegs(List<ComboLegData> comboLegs) {
    return comboLegs.stream()
        .map(
            leg ->
                ComboLegDbo.builder()
                    .contractId(leg.getContractId())
                    .ratio(leg.getRatio())
                    .action(leg.getAction())
                    .exchange(leg.getExchange())
                    .build())
        .collect(Collectors.toCollection(ArrayList::new));
  }
}
