package de.segoy.springboottradingdata.model.data.kafka;

import de.segoy.springboottradingdata.model.data.IBKRDataType;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StandardMarketData extends IBKRDataType {
    private int tickerId;
    private String field;
    private double price;
    private String attrib;
}
