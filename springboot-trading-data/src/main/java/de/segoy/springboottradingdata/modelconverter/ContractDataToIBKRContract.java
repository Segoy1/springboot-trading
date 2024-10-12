package de.segoy.springboottradingdata.modelconverter;

import com.ib.client.ComboLeg;
import com.ib.client.Contract;
import de.segoy.springboottradingdata.model.data.entity.ContractDataDBO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ContractDataToIBKRContract {

    public Contract convertContractData(ContractDataDBO contractDataDBO) {
        Contract contract = new Contract();


        //NotBlankFields
        contract.symbol(contractDataDBO.getSymbol().name());
        contract.secType(contractDataDBO.getSecurityType().toString());
        contract.currency(contractDataDBO.getCurrency());
        contract.exchange(contractDataDBO.getExchange());


        contract.includeExpired(contractDataDBO.isIncludeExpired());
        contract.conid(contractDataDBO.getContractId()==null?0: contractDataDBO.getContractId());

        //Null Values translating to emptyString making things nullsafe
        contract.comboLegsDescrip(contractDataDBO.getComboLegsDescription());
        contract.right(contractDataDBO.getRight()==null?"": contractDataDBO.getRight().toString());
        contract.lastTradeDateOrContractMonth(contractDataDBO.getLastTradeDate()==null?"": contractDataDBO.getLastTradeDate());
        contract.multiplier(contractDataDBO.getMultiplier()==null?"": contractDataDBO.getMultiplier());
        contract.localSymbol(contractDataDBO.getLocalSymbol()==null?"": contractDataDBO.getLocalSymbol());
        contract.tradingClass(contractDataDBO.getTradingClass()==null?"": contractDataDBO.getTradingClass());

        contract.secIdType("");
        contract.secId("");
        contract.issuerId("");
        contract.primaryExch("");


        List<ComboLeg> comboLegs = new ArrayList<>();
        if (contractDataDBO.getComboLegs() != null && !contractDataDBO.getComboLegs().isEmpty()) {
            contractDataDBO.getComboLegs().forEach(comboLegData -> {
                ComboLeg leg = new ComboLeg();
                leg.conid(comboLegData.getContractId());
                leg.ratio(comboLegData.getRatio());
                leg.action(comboLegData.getAction().toString());
                leg.exchange(comboLegData.getExchange());
                comboLegs.add(leg);
            });
            contract.comboLegs(comboLegs);
        }

        if(contractDataDBO.getStrike()!=null){
        contract.strike(contractDataDBO.getStrike().doubleValue());
        }

        return contract;
    }
}
