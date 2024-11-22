package de.segoy.springboottradingdata.service;

import de.segoy.springboottradingdata.constants.AutoDayTradeConstants;
import de.segoy.springboottradingdata.model.subtype.Symbol;
import java.util.List;
import lombok.Builder;
import org.springframework.stereotype.Service;

@Service
public class StrategyComboLegsDescriptionCreator {

    private static final String SEPERATOR = " | ";

    @Builder
    public record StrategyDetails(String lastTradeDate, Symbol symbol, List<Integer> comboLegContractIds){};


    public String generateComboLegsDescription(StrategyDetails details) {
        StringBuilder description = new StringBuilder();
        description
                .append(details.lastTradeDate())
                .append(AutoDayTradeConstants.DELIMITER)
                .append(details.symbol().name())
                .append(SEPERATOR);
        for (Integer contractId : details.comboLegContractIds) {
            description.append(contractId).append(SEPERATOR);
        }
        return description.toString();
    }
}
