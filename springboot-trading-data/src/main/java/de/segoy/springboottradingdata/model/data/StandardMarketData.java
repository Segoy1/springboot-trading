package de.segoy.springboottradingdata.model.data;

import com.ib.client.TickAttrib;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StandardMarketData extends IBKRDataType{
    private int tickerId;
    private int field;
    private double price;
    private TickAttrib attrib;
}
