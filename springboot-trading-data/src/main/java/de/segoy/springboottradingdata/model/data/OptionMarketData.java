package de.segoy.springboottradingdata.model.data;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OptionMarketData extends IBKRDataType {


    private int tickerId;

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
