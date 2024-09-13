package de.segoy.springboottradingweb.spxautotrade.service;

import com.ib.client.Types;
import de.segoy.springboottradingdata.config.TradingConstants;
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
    private static final int CALL_IDENTIFIER = 1;
    private static final int PUT_IDENTIFIER = -1;
    private static final int SPX_TODAY_TICKER_IDENTIFIER = 100000;

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
            ContractData call = builder.strike(BigDecimal.valueOf(strike + strikediff)).right(Types.Right.Call).build();
            callMarketData(call, SPX_TODAY_TICKER_IDENTIFIER + strike + CALL_IDENTIFIER);
            ContractData put = builder.strike(BigDecimal.valueOf(strike - strikediff)).right(Types.Right.Put).build();
            callMarketData(put, SPX_TODAY_TICKER_IDENTIFIER + strike + PUT_IDENTIFIER);
            strikediff += 5;
        }
        while (strikediff <= 100);

    }

    private void callMarketData(ContractData contractData, int tickerId) {
        autoTradeMarketDataService.requestLiveMarketDataForContractData(tickerId,
                contractData);
    }

    private int roundTo5(double price) {
        return (int) Math.round((price / 5) * 5);
    }
}
