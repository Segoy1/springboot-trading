package de.segoy.springboottradingdata.model.data;

import java.util.Map;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OptionChainData  extends IBKRDataType{

    private String lastTradeDate;
    private String symbol;
    private Map<Integer, OptionMarketData> calls;
    private Map<Integer, OptionMarketData> puts;
}
