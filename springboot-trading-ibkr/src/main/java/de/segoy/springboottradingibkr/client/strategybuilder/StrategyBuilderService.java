package de.segoy.springboottradingibkr.client.strategybuilder;

import com.ib.client.Types;
import de.segoy.springboottradingdata.model.data.StrategyContractData;
import de.segoy.springboottradingdata.model.data.entity.ComboLegData;
import de.segoy.springboottradingdata.model.data.entity.ContractData;
import de.segoy.springboottradingdata.repository.ComboLegDataRepository;
import de.segoy.springboottradingibkr.client.service.contract.UniqueContractDataProvider;
import de.segoy.springboottradingdata.model.Leg;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
@RequiredArgsConstructor
public class StrategyBuilderService {

    private final UniqueContractDataProvider uniqueContractDataProvider;
    private final ComboLegDataRepository comboLegDataRepository;

    public Optional<ContractData> getComboLegContractData(StrategyContractData strategyContractData) {
        ContractData contractData = strategyContractData.getContractData();
        try {
            contractData.setComboLegs(legListBuilder(contractData, strategyContractData.getStrategyLegs()));
            setComboLegsDescription(contractData);
            return uniqueContractDataProvider.getExistingContractDataOrCallApi(contractData);
        } catch (NoSuchElementException e) {
            return Optional.empty();
        }
    }

    private void setComboLegsDescription(ContractData contractData) {
        StringBuilder description= new StringBuilder();
        for(ComboLegData leg:contractData.getComboLegs()){
            description.append(leg.getContractId()).append(" | ");
        }
        contractData.setComboLegsDescription(description.toString());
    }

    private List<ComboLegData> legListBuilder(ContractData contractData, List<Leg> legs) {
        List<ComboLegData> legData = new ArrayList<>();

        legs.forEach((leg) -> {
            ContractData legContract = uniqueContractDataProvider.getExistingContractDataOrCallApi(singleLegBuilder(contractData, leg)).orElseThrow();
            legData.add(buildComboLegData(legContract, leg));
        });
        return legData;
    }

    private ComboLegData buildComboLegData(ContractData contractDataBuyPut, Leg leg) {
        return comboLegDataRepository.save(ComboLegData.builder()
                .contractId(contractDataBuyPut.getContractId())
                .ratio(leg.getRatio())
                .action(leg.getAction())
                .exchange(contractDataBuyPut.getExchange())
                .build());
    }

    private ContractData singleLegBuilder(ContractData contractData, Leg leg) {
        return ContractData.builder()
                .symbol(contractData.getSymbol())
                .securityType(Types.SecType.OPT)
                .currency(contractData.getCurrency())
                .exchange(contractData.getExchange())
                .tradingClass(contractData.getTradingClass())
                .strike(BigDecimal.valueOf(leg.getStrike()))
                .right(leg.getRight())
                .lastTradeDate(contractData.getLastTradeDate())
                .build();
    }
}
