package de.segoy.springboottradingdata.model.data.entity;

import com.ib.client.Types;
import de.segoy.springboottradingdata.model.data.IBKRDataType;
import de.segoy.springboottradingdata.model.data.kafka.KafkaComboLegData;
import de.segoy.springboottradingdata.model.data.kafka.KafkaContractData;
import de.segoy.springboottradingdata.model.subtype.Symbol;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContractDbo extends IBKRDataType {

    @Id
    @SequenceGenerator(initialValue = 9000000, allocationSize = 1, name = "contract_id_sequence")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "contract_id_sequence")
    private Long id;

    private Integer contractId; //ContractId From Call to IBKR Api

    @NotNull
    private Symbol symbol; //SPX

    @Enumerated(EnumType.STRING)
    @NotNull
    private Types.SecType securityType; //OPT

    @NotNull
    private String currency; //USD

    private String exchange; // "SMART"

    private String lastTradeDate; //format 20231116
    private BigDecimal strike; // 4100

    @Enumerated(EnumType.STRING)
    @Column(name = "trade_right")
    private Types.Right right; // "C" "P"

    private String multiplier; // "100"
//    private String primaryExchange; //Not SMART //leave empty

    private String localSymbol; // can be used to define options Maybe best solution "P SPX  20231116 4100 W"
    private String tradingClass; //SPXW
//    private String securityIdType; // CUSIP;SEDOL;ISIN;RIC //Leave Empty
//    private String securityId;
//    private String description;
//    private String issuerId;


    private boolean includeExpired;  // can not be set to true for orders
    private String comboLegsDescription; // received in open order version 14 and up for all combos

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name="contract_legs",
            joinColumns = @JoinColumn(name="contract_data_id"),
            inverseJoinColumns = @JoinColumn(name="combo_leg_id")
    )
    private List<ComboLegDbo> comboLegs;


    public KafkaContractData toKafkaContractData() {
        List<KafkaComboLegData> legs = new ArrayList<>();
        if (comboLegs != null && !comboLegs.isEmpty()) {
            comboLegs.forEach((leg) -> legs.add(leg.toKafkaComboLegData()));
        }
        return KafkaContractData.builder()
                .contractId(contractId)
                .symbol(symbol)
                .securityType(securityType)
                .currency(currency)
                .exchange(exchange)
                .lastTradeDate(lastTradeDate)
                .strike(strike)
                .right(right)
                .multiplier(multiplier)
                .localSymbol(localSymbol)
                .tradingClass(tradingClass)
                .includeExpired(includeExpired)
                .comboLegsDescription(comboLegsDescription)
                .comboLegs(legs)
                .build();
    }
}
