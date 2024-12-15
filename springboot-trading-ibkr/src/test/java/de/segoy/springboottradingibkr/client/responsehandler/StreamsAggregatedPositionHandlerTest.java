package de.segoy.springboottradingibkr.client.responsehandler;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import de.segoy.springboottradingdata.model.data.entity.ContractDbo;
import de.segoy.springboottradingdata.model.data.entity.OrderDbo;
import de.segoy.springboottradingdata.model.data.entity.PositionDbo;
import de.segoy.springboottradingdata.model.data.kafka.PositionData;
import de.segoy.springboottradingdata.model.subtype.Symbol;
import de.segoy.springboottradingdata.modelconverter.PositionDataToDbo;
import de.segoy.springboottradingdata.service.PartialComboOrderFinder;
import de.segoy.springboottradingdata.service.PositionSplitService;
import de.segoy.springboottradingibkr.client.service.marketdata.AutoTradeMarketDataService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StreamsAggregatedPositionHandlerTest {

  @Mock private PositionDataToDbo positionDataToDbo;
  @Mock private PartialComboOrderFinder partialComboOrderFinder;
  @Mock private PositionSplitService positionSplitService;
  @Mock private AutoTradeMarketDataService autoTradeMarketDataService;
  @InjectMocks private StreamsAggregatedPositionHandler streamsAggregatedPositionHandler;

  @Test
  void test_SimpleSinglePositionList_ReturnsListAndCallsMarketData() {
    ContractDbo contractDbo =
        ContractDbo.builder().lastTradeDate("20241115").symbol(Symbol.SPX).contractId(1).build();
    PositionDbo positionDbo = PositionDbo.builder().id(1L).contractDBO(contractDbo).build();
    OrderDbo orderDbo = OrderDbo.builder().contractDBO(contractDbo).build();

    when(positionDataToDbo.convert(any())).thenReturn(positionDbo);

    when(partialComboOrderFinder.findExistingStrategyContractsInCombo(positionDbo.getContractDBO()))
        .thenReturn(List.of(orderDbo));
    when(positionSplitService.splitGivenContractsFromPosition(List.of(orderDbo), positionDbo))
        .thenReturn(List.of(positionDbo));

    List<PositionData> result =
        streamsAggregatedPositionHandler.persistPositionsAccordingToExistingOrders(
            positionDbo.toKafkaPositionData());

    verify(autoTradeMarketDataService, times(1))
        .requestLiveMarketDataForContractData(1, contractDbo);
    assertThat(result.get(0).getContractData().getContractId()).isEqualTo(1);
    assertThat(result.get(0).getContractData().getLastTradeDate()).isEqualTo("20241115");
  }

  @Test
  void test_SimpleTwoPositionList_ReturnsListAndCallsMarketData() {
    ContractDbo contractDbo =
        ContractDbo.builder().lastTradeDate("20241115").symbol(Symbol.SPX).contractId(1).build();
    ContractDbo contractDbo2 =
        ContractDbo.builder().lastTradeDate("20241115").symbol(Symbol.SPX).contractId(2).build();
    ContractDbo contractDbo3 =
        ContractDbo.builder().lastTradeDate("20241115").symbol(Symbol.SPX).contractId(3).build();
    PositionDbo positionDbo = PositionDbo.builder().id(1L).contractDBO(contractDbo).build();
    PositionDbo positionDbo2 = PositionDbo.builder().id(2L).contractDBO(contractDbo2).build();
    PositionDbo positionDbo3 = PositionDbo.builder().id(3L).contractDBO(contractDbo3).build();
    List<PositionDbo> positions = List.of(positionDbo2, positionDbo3);
    OrderDbo orderDbo3 = OrderDbo.builder().contractDBO(contractDbo3).build();
    OrderDbo orderDbo2 = OrderDbo.builder().contractDBO(contractDbo2).build();
    List<OrderDbo> orderList = List.of(orderDbo3, orderDbo2);

    when(positionDataToDbo.convert(any())).thenReturn(positionDbo);

    when(partialComboOrderFinder.findExistingStrategyContractsInCombo(positionDbo.getContractDBO()))
        .thenReturn(orderList);
    when(positionSplitService.splitGivenContractsFromPosition(orderList, positionDbo))
        .thenReturn(positions);

    List<PositionData> result =
        streamsAggregatedPositionHandler.persistPositionsAccordingToExistingOrders(
            positionDbo.toKafkaPositionData());

    verify(autoTradeMarketDataService, times(1))
        .requestLiveMarketDataForContractData(2, contractDbo2);
    verify(autoTradeMarketDataService, times(1))
            .requestLiveMarketDataForContractData(3, contractDbo3);

    assertThat(result.size()).isEqualTo(2);
    assertThat(result.get(0).getContractData().getContractId()).isEqualTo(2);
    assertThat(result.get(1).getContractData().getContractId()).isEqualTo(3);
    assertThat(result.get(0).getContractData().getLastTradeDate()).isEqualTo("20241115");
  }
}
