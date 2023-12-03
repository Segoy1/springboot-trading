package de.segoy.springboottradingibkr.client.strategybuilder;

import com.ib.client.Types;
import de.segoy.springboottradingdata.model.ComboLegData;
import de.segoy.springboottradingdata.model.ContractData;
import de.segoy.springboottradingdata.repository.ComboLegDataRepository;
import de.segoy.springboottradingibkr.client.services.UniqueContractDataProvider;
import de.segoy.springboottradingibkr.client.strategybuilder.type.Leg;
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

    public Optional<ContractData> getComboLegContractData(ContractData contractData, Map<Leg, Double> legMap) {
        try {
            contractData.setComboLegs(legListBuilder(contractData, legMap));
            return uniqueContractDataProvider.getExistingContractDataOrCallApi(contractData);
        } catch (NoSuchElementException e) {
            return Optional.empty();
        }
    }

    private List<ComboLegData> legListBuilder(ContractData contractData, Map<Leg, Double> legMap) {
        List<ComboLegData> legs = new ArrayList<>();

        legMap.forEach((leg, strike) -> {
            ContractData legContract = uniqueContractDataProvider.getExistingContractDataOrCallApi(singleLegBuilder(contractData, strike, leg.getRight())).orElseThrow();
            legs.add(buildComboLegData(legContract, leg.getAction(), leg.getRatio()));
        });
        return legs;
    }

    private ComboLegData buildComboLegData(ContractData contractDataBuyPut, Types.Action action, int ratio) {
        return comboLegDataRepository.save(ComboLegData.builder()
                .contractId(contractDataBuyPut.getContractId())
                .ratio(ratio)
                .action(action)
                .exchange(contractDataBuyPut.getExchange())
                .build());
    }

    private ContractData singleLegBuilder(ContractData contractData, Double strike, Types.Right right) {
        return ContractData.builder()
                .symbol(contractData.getSymbol())
                .securityType(Types.SecType.OPT)
                .currency(contractData.getCurrency())
                .exchange(contractData.getExchange())
                .tradingClass(contractData.getTradingClass())
                .strike(BigDecimal.valueOf(strike))
                .right(right)
                .lastTradeDate(contractData.getLastTradeDate())
                .build();
    }
}
