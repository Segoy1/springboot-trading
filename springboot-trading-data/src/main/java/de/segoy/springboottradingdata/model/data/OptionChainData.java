package de.segoy.springboottradingdata.model.data;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OptionChainData  extends IBKRDataType{

    private String lastTradeDate;
    private String symbol;
    private OptionListData calls;
    private OptionListData puts;
}
