package de.segoy.springboottradingibkr.client.strategybuilder;

import com.ib.client.Types;
import de.segoy.springboottradingdata.model.entity.ComboLegData;
import de.segoy.springboottradingdata.model.entity.ContractData;
import de.segoy.springboottradingdata.repository.ComboLegDataRepository;
import de.segoy.springboottradingibkr.client.service.contract.UniqueContractDataProvider;
import de.segoy.springboottradingdata.model.Leg;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class StrategyBuilderService {

    private final UniqueContractDataProvider uniqueContractDataProvider;
    private final ComboLegDataRepository comboLegDataRepository;

    public StrategyBuilderService(UniqueContractDataProvider uniqueContractDataProvider, ComboLegDataRepository comboLegDataRepository) {
        this.uniqueContractDataProvider = uniqueContractDataProvider;
        this.comboLegDataRepository = comboLegDataRepository;
    }

    public Optional<ContractData> getComboLegContractData(ContractData contractData, List<Leg> legs) {
        try {
            contractData.setComboLegs(legListBuilder(contractData, legs));
            return uniqueContractDataProvider.getExistingContractDataOrCallApi(contractData);
        } catch (NoSuchElementException e) {
            return Optional.empty();
        }
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
