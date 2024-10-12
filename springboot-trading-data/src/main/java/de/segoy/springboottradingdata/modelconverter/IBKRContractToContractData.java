package de.segoy.springboottradingdata.modelconverter;

import com.ib.client.Contract;
import de.segoy.springboottradingdata.model.data.entity.ComboLegDataDBO;
import de.segoy.springboottradingdata.model.data.entity.ContractDataDBO;
import de.segoy.springboottradingdata.model.subtype.Symbol;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class IBKRContractToContractData {

    public ContractDataDBO convertIBKRContract(Contract contract) {
        ContractDataDBO contractDataDBO = ContractDataDBO.builder()
                .contractId(contract.conid())
                .symbol(Symbol.valueOf(contract.symbol()))
                .securityType(contract.secType())
                .currency(contract.currency())
                .exchange(contract.exchange()!=null? contract.exchange() : "SMART")

                .right(contract.right())
                .strike(BigDecimal.valueOf(contract.strike()))
                .lastTradeDate(contract.lastTradeDateOrContractMonth())
                .multiplier(contract.multiplier())
                .localSymbol(contract.localSymbol())
                .tradingClass(contract.tradingClass())
                .includeExpired(contract.includeExpired())
                .comboLegsDescription(contract.comboLegsDescrip())
                .build();

        List<ComboLegDataDBO> legs = new ArrayList<>();
        contract.comboLegs().forEach(comboLeg -> {
        ComboLegDataDBO leg = ComboLegDataDBO.builder()
                .contractId(comboLeg.conid())
                .exchange(comboLeg.exchange())
                .ratio(comboLeg.ratio())
                .action(comboLeg.action())
                .build();
            legs.add(leg);
        });

        contractDataDBO.setComboLegs(legs);
        //Builder from Super Class not inherited
//        contractData.setTouchedByApi(true);

        return contractDataDBO;
    }
}
