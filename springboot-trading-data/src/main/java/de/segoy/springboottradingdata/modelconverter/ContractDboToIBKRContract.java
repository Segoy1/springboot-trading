package de.segoy.springboottradingdata.modelconverter;

import com.ib.client.ComboLeg;
import com.ib.client.Contract;
import de.segoy.springboottradingdata.model.data.entity.ContractDbo;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ContractDboToIBKRContract {

    private static final String EMPTY = "";

    public Contract convertContractData(ContractDbo contractDBO) {
        Contract contract = new Contract();


        //NotBlankFields
        contract.symbol(contractDBO.getSymbol().name());
        contract.secType(contractDBO.getSecurityType().toString());
        contract.currency(contractDBO.getCurrency());
        contract.exchange(contractDBO.getExchange());


        contract.includeExpired(contractDBO.isIncludeExpired());
        contract.conid(contractDBO.getContractId()==null?0: contractDBO.getContractId());

        //Null Values translating to emptyString making things nullsafe
        contract.comboLegsDescrip(contractDBO.getComboLegsDescription());
        contract.right(contractDBO.getRight()==null?EMPTY: contractDBO.getRight().toString());
        contract.lastTradeDateOrContractMonth(contractDBO.getLastTradeDate()==null?EMPTY: contractDBO.getLastTradeDate());
        contract.multiplier(contractDBO.getMultiplier()==null?EMPTY: contractDBO.getMultiplier());
        contract.localSymbol(contractDBO.getLocalSymbol()==null?EMPTY: contractDBO.getLocalSymbol());
        contract.tradingClass(contractDBO.getTradingClass()==null?EMPTY: contractDBO.getTradingClass());

        contract.secIdType(EMPTY);
        contract.secId(EMPTY);
        contract.issuerId(EMPTY);
        contract.primaryExch(EMPTY);


        List<ComboLeg> comboLegs = new ArrayList<>();
        if (contractDBO.getComboLegs() != null && !contractDBO.getComboLegs().isEmpty()) {
            contractDBO.getComboLegs().forEach(comboLegData -> {
                ComboLeg leg = new ComboLeg();
                leg.conid(comboLegData.getContractId());
                leg.ratio(comboLegData.getRatio());
                leg.action(comboLegData.getAction().toString());
                leg.exchange(comboLegData.getExchange());
                comboLegs.add(leg);
            });
            contract.comboLegs(comboLegs);
        }

        if(contractDBO.getStrike()!=null){
        contract.strike(contractDBO.getStrike().doubleValue());
        }

        return contract;
    }
}
