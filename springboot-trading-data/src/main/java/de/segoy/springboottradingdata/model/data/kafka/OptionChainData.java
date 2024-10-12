package de.segoy.springboottradingdata.model.data.kafka;

import de.segoy.springboottradingdata.model.data.IBKRDataType;
import de.segoy.springboottradingdata.model.subtype.Symbol;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OptionChainData  extends IBKRDataType {

    private String lastTradeDate;
    private Symbol symbol;
    private OptionListData calls;
    private OptionListData puts;
}
