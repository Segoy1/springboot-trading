package de.segoy.springboottradingdata.model;

import com.ib.client.Types;
import de.segoy.springboottradingdata.type.Currency;
import de.segoy.springboottradingdata.type.Symbol;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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

    @Enumerated(EnumType.STRING)
    @NotBlank
    private Symbol symbol; //SPX

    @Enumerated(EnumType.STRING)
    @NotBlank
    private Types.SecType securityType; //OPT

    @Enumerated(EnumType.STRING)
    @NotBlank
    private Currency currency; //USD

    @NotBlank
    private String exchange; // "SMART"

    private String lastTradeDateOrContractMonth; //format 20231116
    private BigDecimal strike; // 4100

    @Enumerated(EnumType.STRING)
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
    private List<ComboLegData> comboLegs;
}
