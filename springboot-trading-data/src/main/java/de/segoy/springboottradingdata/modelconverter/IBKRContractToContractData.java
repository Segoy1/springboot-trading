package de.segoy.springboottradingdata.modelconverter;

import com.ib.client.Contract;
import de.segoy.springboottradingdata.model.ComboLegData;
import de.segoy.springboottradingdata.model.ContractData;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class IBKRContractToContractData {

    public ContractData convertIBKRContract(Contract contract) {
        ContractData contractData = ContractData.builder()
                .contractId(contract.conid())
                .symbol(contract.symbol())
                .securityType(contract.secType())
                .currency(contract.currency())
                .exchange(contract.exchange())

                .right(contract.right())
                .strike(BigDecimal.valueOf(contract.strike()))
                .lastTradeDate(contract.lastTradeDateOrContractMonth())
                .multiplier(contract.multiplier())
                .localSymbol(contract.localSymbol())
                .tradingClass(contract.tradingClass())
                .includeExpired(contract.includeExpired())
                .comboLegsDescription(contract.comboLegsDescrip())
                .build();

        List<ComboLegData> legs = new ArrayList<>();
        contract.comboLegs().forEach(comboLeg -> {
        ComboLegData leg = ComboLegData.builder()
                .contractId(comboLeg.conid())
                .exchange(comboLeg.exchange())
                .ratio(comboLeg.ratio())
                .action(comboLeg.action())
                .build();
            legs.add(leg);
        });

        contractData.setComboLegs(legs);
        //Builder from Super Class not inherited
//        contractData.setTouchedByApi(true);

        return contractData;
    }
}
