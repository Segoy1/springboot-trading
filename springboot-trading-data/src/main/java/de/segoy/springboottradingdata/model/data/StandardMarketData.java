package de.segoy.springboottradingdata.model.data;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StandardMarketData extends IBKRDataType{
    private int tickerId;
    private String field;
    private double price;
    private String attrib;
}
