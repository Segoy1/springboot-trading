package de.segoy.springboottradingweb.spxautotrade.service;

import com.ib.client.Types;
import de.segoy.springboottradingdata.config.TradingConstants;
import de.segoy.springboottradingdata.constants.AutoDayTradeConstants;
import de.segoy.springboottradingdata.model.data.entity.ContractData;
import de.segoy.springboottradingdata.service.LastTradeDateBuilder;
import de.segoy.springboottradingibkr.client.service.marketdata.AutoTradeMarketDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class AutoTradeCallAndPutDataRequestService {

    private final AutoTradeMarketDataService autoTradeMarketDataService;
    private final LastTradeDateBuilder lastTradeDateBuilder;


    public void getOptionContractsAndCallAPI(double price) {
        int strike = roundTo5(price);
        int strikediff = 5;
        do {
            ContractData.ContractDataBuilder builder =
                    ContractData.builder().securityType(Types
                                    .SecType.OPT)
                            .symbol(TradingConstants.SPX)
                            .exchange(TradingConstants.CBOE)
                            .currency(TradingConstants.USD)
                            .lastTradeDate(lastTradeDateBuilder.getDateStringFromToday())
                            .tradingClass(TradingConstants.SPXW);
            int callPrice = strike + strikediff;
            ContractData call = builder.strike(BigDecimal.valueOf(callPrice)).right(Types.Right.Call).build();
            callMarketData(call,
                    AutoDayTradeConstants.SPX_TODAY_TICKER_IDENTIFIER + callPrice + AutoDayTradeConstants.CALL_TICKER_IDENTIFIER);

            int putPrice = strike - strikediff;
            ContractData put = builder.strike(BigDecimal.valueOf(putPrice)).right(Types.Right.Put).build();
            callMarketData(put,
                    AutoDayTradeConstants.SPX_TODAY_TICKER_IDENTIFIER + putPrice + AutoDayTradeConstants.PUT_TICKER_IDENTIFIER);
            strikediff += 5;
        }
        while (strikediff <= 100);

    }

    private void callMarketData(ContractData contractData, int tickerId) {
        autoTradeMarketDataService.requestLiveMarketDataForContractData(tickerId,
                contractData);
    }

    private int roundTo5(double price) {
        return (int) (Math.round((price / 5)) * 5);
    }
}
