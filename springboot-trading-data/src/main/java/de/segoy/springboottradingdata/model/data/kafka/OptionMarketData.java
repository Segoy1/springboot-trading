package de.segoy.springboottradingdata.model.data.kafka;

import com.ib.client.Types;
import de.segoy.springboottradingdata.model.subtype.Symbol;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OptionMarketData extends KafkaDataType {


    private int tickerId;
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
