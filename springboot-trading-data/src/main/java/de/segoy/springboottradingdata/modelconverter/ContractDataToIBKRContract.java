package de.segoy.springboottradingdata.modelconverter;

import com.ib.client.ComboLeg;
import com.ib.client.Contract;
import de.segoy.springboottradingdata.model.ContractData;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ContractDataToIBKRContract {

    public Contract convertContractData(ContractData contractData) {
        Contract contract = new Contract();


        //NotBlankFields
        contract.symbol(contractData.getSymbol().toString());
        contract.secType(contractData.getSecurityType().toString());
        contract.currency(contractData.getCurrency().toString());
        contract.exchange(contractData.getExchange());


        contract.right(contractData.getRight().toString());
        contract.lastTradeDateOrContractMonth(contractData.getLastTradeDateOrContractMonth());
        contract.multiplier(contractData.getMultiplier());
        contract.localSymbol(contractData.getLocalSymbol());
        contract.tradingClass(contractData.getTradingClass());
        contract.includeExpired(contractData.isIncludeExpired());
        contract.comboLegsDescrip(contractData.getComboLegsDescription());

        List<ComboLeg> comboLegs = new ArrayList<>();
        if (!contractData.getComboLegs().isEmpty()) {
            contractData.getComboLegs().forEach(comboLegData -> {
                ComboLeg leg = new ComboLeg();
                leg.conid(comboLegData.getContractId());
                leg.ratio(comboLegData.getRatio());
                leg.action(comboLegData.getAction().toString());
                leg.exchange(comboLegData.getExchange());
                comboLegs.add(leg);
            });
            contract.comboLegs(comboLegs);
        }

        if(contractData.getStrike()!=null){
        contract.strike(contractData.getStrike().doubleValue());
        }

        return contract;
    }
}
