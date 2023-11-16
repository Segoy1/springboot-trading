package de.segoy.springboottradingdata.model;

import de.segoy.springboottradingdata.type.Right;
import de.segoy.springboottradingdata.type.SecurityType;
import de.segoy.springboottradingdata.type.Symbol;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private Symbol symbol; //SPX

    @Enumerated(EnumType.STRING)
    private SecurityType securityType; //OPT

    private String lastTradeDateOrContractMonth; //format 20231116
    private BigDecimal strike; // 4100

    @Enumerated(EnumType.STRING)
    private Right right; // "C" "P"

    private BigDecimal multiplier; // "100"
    private String exchange; // "SMART"
    private String primaryExchange; //Not SMART //leave empty

    @Enumerated(EnumType.STRING)
    private String currency; //USD
    private String localSymbol; // can be used to define options Maybe best solution "P SPX  20231116 4100 W"
    private String tradingClass; //SPXW
    private String securityIdType; // CUSIP;SEDOL;ISIN;RIC //Leave Empty
    private String securityId;
    private String description;
    private String issuerId;


    private boolean m_includeExpired;  // can not be set to true for orders
    // COMBOS
    private String m_comboLegsDescrip; // received in open order version 14 and up for all combos

    @OneToMany
    private List<ComboLegData> m_comboLegs;
}
