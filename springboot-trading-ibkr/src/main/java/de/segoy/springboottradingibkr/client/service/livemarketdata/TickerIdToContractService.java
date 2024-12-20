package de.segoy.springboottradingibkr.client.service.livemarketdata;

import com.ib.client.Types;
import de.segoy.springboottradingdata.config.PropertiesConfig;
import de.segoy.springboottradingdata.config.TradeRuleSettingsConfig;
import de.segoy.springboottradingdata.constants.AutoDayTradeConstants;
import de.segoy.springboottradingdata.dataobject.ContractDataTemplates;
import de.segoy.springboottradingdata.model.data.entity.ContractDbo;
import de.segoy.springboottradingdata.model.subtype.Symbol;
import de.segoy.springboottradingdata.optionstradingservice.LastTradeDateBuilder;
import de.segoy.springboottradingdata.repository.ContractRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TickerIdToContractService {

    private final ContractRepository contractRepository;
    private final PropertiesConfig propertiesConfig;
    private final LastTradeDateBuilder lastTradeDateBuilder;
    private final TradeRuleSettingsConfig tradeRuleSettingsConfig;

    public ContractDbo resolveTickerId(int tickerId){
        if (tickerId == propertiesConfig.getSpxTickerId()){
            return contractRepository
                    .findFirstBySymbolAndSecurityTypeAndCurrency(Symbol.SPX, Types.SecType.IND, "USD")
                    .orElseGet(() -> contractRepository.save(ContractDataTemplates.SpxOptionData()));
        }else if(Integer.toString(tickerId).endsWith(lastTradeDateBuilder.getShortenedDateStringFromToday())){
            //TODO for Strategy expansion: add Strategy Name to Description and check here
            String search =
                    lastTradeDateBuilder.getDateStringFromToday()+ AutoDayTradeConstants.DELIMITER+tradeRuleSettingsConfig.getTradeSymbol();
            return contractRepository.findByComboLegsDescriptionContains(search).get(0);
        }else{
            throw new RuntimeException("No Contract fo Ticker found");
        }
    }
}
