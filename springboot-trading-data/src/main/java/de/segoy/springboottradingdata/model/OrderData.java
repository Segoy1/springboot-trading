package de.segoy.springboottradingdata.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderData {

    @Id
    private Integer orderId;

    private String m_action;
    private BigDecimal m_totalQuantity;
    private String m_orderType;
    private BigDecimal m_lmtPrice;
    private BigDecimal m_auxPrice;
    private String m_tif; // "Time in Force" - DAY, GTC, etc.
    // native cash quantity
    private BigDecimal m_cashQty;
    private Boolean m_usePriceMgmtAlgo;
}
