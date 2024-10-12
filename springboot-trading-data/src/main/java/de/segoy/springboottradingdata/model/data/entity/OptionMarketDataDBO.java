package de.segoy.springboottradingdata.model.data.entity;

import com.ib.client.Types;
import de.segoy.springboottradingdata.model.data.IBKRDataType;
import de.segoy.springboottradingdata.model.subtype.Symbol;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OptionMarketDataDBO extends IBKRDataType {

    @Id
    private Long tickerId;
    private double strike;
    private Types.Right right;
    private Symbol symbol;
    private String lastTradeDate;

    //Resolve with  TickType.getField( field);
    //10 = bid 11= ask 12=last 13=model
    private String field;
    private int tickAttrib;
    private double impliedVol;
    private double delta;
    private double optPrice;
    private double pvDividend;
    private double gamma;
    private double vega;
    private double theta;
    private double undPrice;
}
