package de.segoy.springboottradingweb.spxautotrade.service;

import com.ib.client.Types;
import de.segoy.springboottradingdata.config.TradingConstants;
import de.segoy.springboottradingdata.model.data.entity.ContractDbo;
import de.segoy.springboottradingdata.model.subtype.Symbol;
import de.segoy.springboottradingdata.optionstradingservice.LastTradeDateBuilder;
import de.segoy.springboottradingibkr.client.service.marketdata.StartMarketDataService;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AutoTradeCallAndPutDataRequestService {

    private final StartMarketDataService startMarketDataService;
    private final LastTradeDateBuilder lastTradeDateBuilder;


    public void getOptionContractsAndCallAPI(double price) {
        int strike = roundTo5(price);
        int strikediff = 5;
        do {
            ContractDbo.ContractDboBuilder builder =
                    ContractDbo.builder().securityType(Types
                                    .SecType.OPT)
                            .symbol(Symbol.SPX)
                            .exchange(TradingConstants.CBOE)
                            .currency(TradingConstants.USD)
                            .lastTradeDate(lastTradeDateBuilder.getDateStringFromToday())
                            .tradingClass(Symbol.SPXW.name());
            int callPrice = strike + strikediff;
            ContractDbo call = builder.strike(BigDecimal.valueOf(callPrice)).right(Types.Right.Call).build();
            callMarketData(call);

            int putPrice = strike - strikediff;
            ContractDbo put = builder.strike(BigDecimal.valueOf(putPrice)).right(Types.Right.Put).build();
            callMarketData(put);
            strikediff += 5;
        }
        while (strikediff <= 100);

    }

    private void callMarketData(ContractDbo contractDBO) {
        startMarketDataService.requestLiveMarketDataForContractData(contractDBO);
    }

    private int roundTo5(double price) {
        return (int) (Math.round((price / 5)) * 5);
    }
}
