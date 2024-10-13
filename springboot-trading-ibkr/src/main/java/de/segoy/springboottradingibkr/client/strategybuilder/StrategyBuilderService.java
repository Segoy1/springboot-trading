package de.segoy.springboottradingibkr.client.strategybuilder;

import com.ib.client.Types;
import de.segoy.springboottradingdata.model.data.StrategyContractData;
import de.segoy.springboottradingdata.model.data.entity.ComboLegDataDBO;
import de.segoy.springboottradingdata.model.data.entity.ContractDataDBO;
import de.segoy.springboottradingdata.repository.ComboLegDataRepository;
import de.segoy.springboottradingibkr.client.service.contract.UniqueContractDataProvider;
import de.segoy.springboottradingdata.model.Leg;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
@RequiredArgsConstructor
public class StrategyBuilderService {

    private final UniqueContractDataProvider uniqueContractDataProvider;
    private final ComboLegDataRepository comboLegDataRepository;

    @Transactional
    public Optional<ContractDataDBO> getComboLegContractData(StrategyContractData strategyContractData) {
        ContractDataDBO contractDataDBO = strategyContractData.getContractDataDBO();
        try {
            contractDataDBO.setComboLegs(legListBuilder(contractDataDBO, strategyContractData.getStrategyLegs()));
            setComboLegsDescription(contractDataDBO);
            return uniqueContractDataProvider.getExistingContractDataOrCallApi(contractDataDBO);
        } catch (NoSuchElementException e) {
            return Optional.empty();
        }
    }

    private void setComboLegsDescription(ContractDataDBO contractDataDBO) {
        StringBuilder description= new StringBuilder();
        for(ComboLegDataDBO leg: contractDataDBO.getComboLegs()){
            description.append(leg.getContractId()).append(" | ");
        }
        contractDataDBO.setComboLegsDescription(description.toString());
    }

    private List<ComboLegDataDBO> legListBuilder(ContractDataDBO contractDataDBO, List<Leg> legs) {
        List<ComboLegDataDBO> legData = new ArrayList<>();

        legs.forEach((leg) -> {
            ContractDataDBO legContract = uniqueContractDataProvider.getExistingContractDataOrCallApi(singleLegBuilder(
                    contractDataDBO, leg)).orElseThrow();
            legData.add(buildComboLegData(legContract, leg));
        });
        return legData;
    }

    private ComboLegDataDBO buildComboLegData(ContractDataDBO contractDataDBOBuyPut, Leg leg) {
           return     ComboLegDataDBO.builder()
                .contractId(contractDataDBOBuyPut.getContractId())
                .ratio(leg.getRatio())
                .action(leg.getAction())
                .exchange(contractDataDBOBuyPut.getExchange())
                .build();
    }

    private ContractDataDBO singleLegBuilder(ContractDataDBO contractDataDBO, Leg leg) {
        return ContractDataDBO.builder()
                .symbol(contractDataDBO.getSymbol())
                .securityType(Types.SecType.OPT)
                .currency(contractDataDBO.getCurrency())
                .exchange(contractDataDBO.getExchange())
                .tradingClass(contractDataDBO.getTradingClass())
                .strike(BigDecimal.valueOf(leg.getStrike()))
                .right(leg.getRight())
                .lastTradeDate(contractDataDBO.getLastTradeDate())
                .build();
    }
}
