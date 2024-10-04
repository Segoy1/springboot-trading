package de.segoy.springboottradingdata.modelconverter;

import com.ib.client.ComboLeg;
import com.ib.client.Contract;
import de.segoy.springboottradingdata.model.data.entity.ContractData;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ContractDataToIBKRContract {

    public Contract convertContractData(ContractData contractData) {
        Contract contract = new Contract();


        //NotBlankFields
        contract.symbol(contractData.getSymbol().name());
        contract.secType(contractData.getSecurityType().toString());
        contract.currency(contractData.getCurrency());
        contract.exchange(contractData.getExchange());


        contract.includeExpired(contractData.isIncludeExpired());
        contract.conid(contractData.getContractId()==null?0:contractData.getContractId());

        //Null Values translating to emptyString making things nullsafe
        contract.comboLegsDescrip(contractData.getComboLegsDescription());
        contract.right(contractData.getRight()==null?"":contractData.getRight().toString());
        contract.lastTradeDateOrContractMonth(contractData.getLastTradeDate()==null?"":contractData.getLastTradeDate());
        contract.multiplier(contractData.getMultiplier()==null?"":contractData.getMultiplier());
        contract.localSymbol(contractData.getLocalSymbol()==null?"":contractData.getLocalSymbol());
        contract.tradingClass(contractData.getTradingClass()==null?"":contractData.getTradingClass());

        contract.secIdType("");
        contract.secId("");
        contract.issuerId("");
        contract.primaryExch("");


        List<ComboLeg> comboLegs = new ArrayList<>();
        if (contractData.getComboLegs() != null && !contractData.getComboLegs().isEmpty()) {
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
