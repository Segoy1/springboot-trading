package de.segoy.springboottradingweb.spxautotrade.service.order;

import com.ib.client.OrderType;
import com.ib.client.Types;
import de.segoy.springboottradingdata.config.TradeRuleSettingsConfig;
import de.segoy.springboottradingdata.model.data.entity.LastPriceLiveMarketDataDbo;
import de.segoy.springboottradingdata.model.data.entity.OrderDbo;
import de.segoy.springboottradingdata.model.subtype.Strategy;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderCreateAutoTradeService {
  private final TradeRuleSettingsConfig tradeRuleSettingsConfig;

  public OrderDbo setupOrderWithLmtPriceEqualToBidPricePlusTolerance(
      LastPriceLiveMarketDataDbo lastPriceLiveMarketDataDbo, Strategy strategy) {
    double limitPrice =
        lastPriceLiveMarketDataDbo.getBidPrice()
            + tradeRuleSettingsConfig.getToleranceForOrderFill();
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
}
