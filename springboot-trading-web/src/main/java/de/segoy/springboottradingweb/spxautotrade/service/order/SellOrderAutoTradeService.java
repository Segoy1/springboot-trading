package de.segoy.springboottradingweb.spxautotrade.service.order;

import com.ib.client.OrderType;
import com.ib.client.Types;
import de.segoy.springboottradingdata.config.TradeRuleSettingsConfig;
import de.segoy.springboottradingdata.model.data.entity.OrderDbo;
import de.segoy.springboottradingdata.model.data.entity.PositionDbo;
import de.segoy.springboottradingibkr.client.service.order.OrderPlacementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SellOrderAutoTradeService {

  private final TradeRuleSettingsConfig tradeRuleSettingsConfig;
  private final OrderPlacementService orderPlacementService;

  public void sellPostionWhenPriceExceedsLimit(Double price, PositionDbo positionDbo) {
    if (price - tradeRuleSettingsConfig.getToleranceForOrderFill()
        > positionDbo.getAverageCost() * tradeRuleSettingsConfig.getSellThreshold()) {
      orderPlacementService.placeOrderWithAutoIdIfNotSet(
          OrderDbo.builder()
              .id(positionDbo.getId())
              .orderType(OrderType.MKT)
              .contractDBO(positionDbo.getContractDBO())
              .totalQuantity(positionDbo.getPosition())
              .action(Types.Action.SELL)
              .usePriceManagementAlgorithm(false)
              .timeInForce(Types.TimeInForce.DAY)
              .build());
    }
  }
}
