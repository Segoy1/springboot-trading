package de.segoy.springboottradingibkr.client.responsehandler;

import de.segoy.springboottradingdata.model.data.entity.OrderDbo;
import de.segoy.springboottradingdata.model.data.entity.PositionDbo;
import de.segoy.springboottradingdata.model.data.kafka.PositionData;
import de.segoy.springboottradingdata.modelconverter.PositionDataToDbo;
import de.segoy.springboottradingdata.service.PartialComboOrderFinder;
import de.segoy.springboottradingdata.service.PositionSplitService;
import de.segoy.springboottradingibkr.client.service.marketdata.AutoTradeMarketDataService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class StreamsAggregatedPositionHandler {

  private final PositionDataToDbo positionDataToDbo;
  private final PartialComboOrderFinder partialComboOrderFinder;
  private final PositionSplitService positionSplitService;
  private final AutoTradeMarketDataService autoTradeMarketDataService;

  @Transactional
  public List<PositionData> persistPositionsAccordingToExistingOrders(PositionData positionData) {
    PositionDbo positionDbo = positionDataToDbo.convert(positionData);
    List<OrderDbo> filledStrategyOrders =
        partialComboOrderFinder.findExistingStrategyContractsInCombo(positionDbo.getContractDBO());

    List<PositionDbo> splitPositions =
        positionSplitService.splitGivenContractsFromPosition(filledStrategyOrders, positionDbo);

    splitPositions.forEach(
        (position) -> {
          if (position.getId() != null) {
            autoTradeMarketDataService.requestLiveMarketDataForContractData(
                position.getId().intValue(), position.getContractDBO());
          }
        });

    return splitPositions.stream().map(PositionDbo::toKafkaPositionData).toList();
  }
}
