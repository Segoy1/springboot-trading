package de.segoy.springboottradingdata.model;

import com.ib.client.Types;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContractData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer contractId; //ContractId From Call to IBKR Api

    @NotNull
    private String symbol; //SPX

    @Enumerated(EnumType.STRING)
    @NotNull
    private Types.SecType securityType; //OPT

    @NotNull
    private String currency; //USD

    @NotNull
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
    // COMBOS
    private String comboLegsDescription; // received in open order version 14 and up for all combos

    @OneToMany
    @Singular
    @Column(name = "combo_legs")
    private List<ComboLegData> comboLegs;

    private boolean touchedByApi;
}
