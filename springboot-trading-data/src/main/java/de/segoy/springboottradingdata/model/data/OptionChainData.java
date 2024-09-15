package de.segoy.springboottradingdata.model.data;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OptionChainData  extends IBKRDataType{

    private String lastTradeDate;
    private String symbol;
    private Set<OptionMarketData> calls;
    private Set<OptionMarketData> puts;
}
