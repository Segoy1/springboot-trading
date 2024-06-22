package de.segoy.springboottradingweb.spxautotrade.service;

import com.ib.client.Types;
import de.segoy.springboottradingdata.config.TradingConstants;
import de.segoy.springboottradingdata.model.data.entity.ContractData;
import de.segoy.springboottradingdata.service.LastTradingDateBuilder;
import de.segoy.springboottradingibkr.client.service.marketdata.AutoTradeMarketDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class AutoTradeCallAndPutDataRequestService {

    private final AutoTradeMarketDataService autoTradeMarketDataService;
    private final LastTradingDateBuilder lastTradingDateBuilder;

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
                            .lastTradeDate(lastTradingDateBuilder.getDateStringFromToday())
                            .tradingClass(TradingConstants.SPXW);
            ContractData call = builder.strike(BigDecimal.valueOf(strike + strikediff)).right(Types.Right.Call).build();
            callMarketData(call);
            ContractData put = builder.strike(BigDecimal.valueOf(strike - strikediff)).right(Types.Right.Put).build();
            callMarketData(put);
            strikediff += 5;
        }
        while (strikediff <= 100);

    }

    private void callMarketData(ContractData contractData) {
        autoTradeMarketDataService.requestLiveMarketDataForContractData(lastTradingDateBuilder.getDateIntFromToday(),
                contractData);
    }

    private int roundTo5(double price) {
        return (int) Math.round((price / 5) * 5);
    }
}
