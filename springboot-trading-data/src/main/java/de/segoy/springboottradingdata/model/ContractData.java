package de.segoy.springboottradingdata.model;

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

    private String symbol;
    private String securityType;
    private String lastTradeDateOrContractMonth;
    private BigDecimal strike;
    private String right;
    private BigDecimal multiplier;
    private String exchange;
    private String primaryExchange; //Not SMART
    private String currency;
    private String localSymbol;
    private String tradingClass;
    private String securityIdType; // CUSIP;SEDOL;ISIN;RIC
    private String securityId;
    private String description;
    private String issuerId;


    private boolean m_includeExpired;  // can not be set to true for orders
    // COMBOS
    private String m_comboLegsDescrip; // received in open order version 14 and up for all combos

    @OneToMany
    private List<ComboLegData> m_comboLegs;
}
