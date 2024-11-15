package de.segoy.springboottradingweb.spxautotrade.service.order;

import com.ib.client.OrderType;
import com.ib.client.Types;
import de.segoy.springboottradingdata.config.TradeRuleSettingsConfig;
import de.segoy.springboottradingdata.constants.AutoDayTradeConstants;
import de.segoy.springboottradingdata.model.data.entity.LastPriceLiveMarketDataDbo;
import de.segoy.springboottradingdata.model.data.entity.OrderDbo;
import de.segoy.springboottradingdata.model.subtype.Strategy;
import de.segoy.springboottradingdata.model.subtype.Symbol;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderCreateAutoTradeService {
  private final TradeRuleSettingsConfig tradeRuleSettingsConfig;

  public OrderDbo setupOrderWithLmtPriceHalfwayBetweenBidAndAsk(
      LastPriceLiveMarketDataDbo lastPriceLiveMarketDataDbo, Strategy strategy) {
    double limitPrice = getLimitPriceForSecurity(lastPriceLiveMarketDataDbo);
    return OrderDbo.builder()
        .contractDBO(lastPriceLiveMarketDataDbo.getContractDBO())
        .action(Types.Action.BUY)
        .totalQuantity(BigDecimal.valueOf(tradeRuleSettingsConfig.getQuantity()))
        .limitPrice(BigDecimal.valueOf(limitPrice))
        .orderType(OrderType.LMT)
        .usePriceManagementAlgorithm(false)
        .timeInForce(Types.TimeInForce.DAY)
        .build();
  }

  private double getLimitPriceForSecurity(LastPriceLiveMarketDataDbo dbo) {
    double multi;
    double limit = (dbo.getBidPrice() + dbo.getAskPrice()) / 2;
    if (dbo.getContractDBO().getSymbol().equals(Symbol.SPX)) {
      multi = AutoDayTradeConstants.SPX_OPTION_PRICE_MULTIPLIER;
    } else {
      multi = AutoDayTradeConstants.STOCK_OPTION_PRICE_MULTIPLIER;
    }
    return Math.ceil(limit / multi) * multi;
  }
}
