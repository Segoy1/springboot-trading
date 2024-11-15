package de.segoy.springboottradingibkr.client.responsehandler;

import de.segoy.springboottradingdata.config.TradeRuleSettingsConfig;
import de.segoy.springboottradingdata.model.data.entity.ContractDbo;
import de.segoy.springboottradingdata.model.data.entity.PositionDbo;
import de.segoy.springboottradingdata.model.data.kafka.PositionData;
import de.segoy.springboottradingdata.model.subtype.Strategy;
import de.segoy.springboottradingdata.model.subtype.Symbol;
import de.segoy.springboottradingdata.modelconverter.PositionDataToDbo;
import de.segoy.springboottradingdata.modelsynchronize.PositionDataDatabaseSynchronizer;
import de.segoy.springboottradingdata.optionstradingservice.AutotradeDbAndTickerIdEncoder;
import de.segoy.springboottradingdata.optionstradingservice.LastTradeDateBuilder;
import de.segoy.springboottradingdata.service.StrategyNameService;
import de.segoy.springboottradingibkr.client.service.contract.UniqueContractDataProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StreamsAggregatedPositionHandlerTest {

  @Mock private UniqueContractDataProvider uniqueContractDataProvider;
  @Mock private PositionDataDatabaseSynchronizer positionDataDatabaseSynchronizer;
  @Mock private PositionDataToDbo positionDataToDbo;
  @Mock private LastTradeDateBuilder lastTradeDateBuilder;
  @Mock private TradeRuleSettingsConfig tradeRuleSettingsConfig;
  @Mock private StrategyNameService strategyNameService;
  @Mock private AutotradeDbAndTickerIdEncoder autotradeDbAndTickerIdEncoder;
  @InjectMocks private StreamsAggregatedPositionHandler streamsAggregatedPositionHandler;

  @Test
  void testError() {
    ContractDbo contractDboOld = ContractDbo.builder().contractId(1).build();
    PositionDbo positionDBO = PositionDbo.builder().contractDBO(contractDboOld).build();
    ContractDbo contractDboNew = ContractDbo.builder().contractId(2).build();

    when(uniqueContractDataProvider.getExistingContractDataOrCallApi(contractDboOld))
        .thenReturn(Optional.empty());
    when(positionDataToDbo.convert(any())).thenReturn(positionDBO);

    Exception e =
        assertThrows(
            RuntimeException.class,
            () -> {
              streamsAggregatedPositionHandler.persistContractAndPositionData(
                  positionDBO.toKafkaPositionData());
            });

    assertThat(e.getMessage()).isEqualTo("No value present");
  }

  @Test
  void testPersistWithId() {
    ContractDbo contractDboOld =
        ContractDbo.builder().lastTradeDate("20241115").symbol(Symbol.SPX).contractId(1).build();
    PositionDbo positionDBO = PositionDbo.builder().contractDBO(contractDboOld).build();
    ContractDbo contractDboNew =
        ContractDbo.builder().lastTradeDate("20241115").symbol(Symbol.SPX).contractId(2).build();

    when(uniqueContractDataProvider.getExistingContractDataOrCallApi(contractDboOld))
        .thenReturn(Optional.of(contractDboNew));
    when(tradeRuleSettingsConfig.getTradeSymbol()).thenReturn(Symbol.SPX);
    when(positionDataDatabaseSynchronizer.updateInDbOrSave(positionDBO))
        .thenReturn(Optional.of(positionDBO));
    when(positionDataToDbo.convert(any())).thenReturn(positionDBO);
    when(lastTradeDateBuilder.getDateStringFromToday()).thenReturn("20241115");
    when(strategyNameService.resolveStrategyFromComboLegs(any())).thenReturn(Strategy.IRON_CONDOR);
    when(autotradeDbAndTickerIdEncoder.generateLongForTodayBySymbolAndStrategy(
            Symbol.SPX, Strategy.IRON_CONDOR))
        .thenReturn(31241115L);

    Optional<PositionData> result =
        streamsAggregatedPositionHandler.persistContractAndPositionData(
            positionDBO.toKafkaPositionData());

    assertThat(result.get().getContractData().getContractId()).isEqualTo(2);
  }

  @Test
  void testPersistWithOutId() {
    ContractDbo contractDboOld =
        ContractDbo.builder().lastTradeDate("20241116").symbol(Symbol.SPX).contractId(1).build();
    PositionDbo positionDBO = PositionDbo.builder().contractDBO(contractDboOld).build();
    ContractDbo contractDboNew =
        ContractDbo.builder().lastTradeDate("20241116").symbol(Symbol.SPX).contractId(2).build();

    when(uniqueContractDataProvider.getExistingContractDataOrCallApi(contractDboOld))
        .thenReturn(Optional.of(contractDboNew));
    when(positionDataDatabaseSynchronizer.updateInDbOrSave(positionDBO))
        .thenReturn(Optional.of(positionDBO));
    when(positionDataToDbo.convert(any())).thenReturn(positionDBO);
    when(lastTradeDateBuilder.getDateStringFromToday()).thenReturn("20241115");

    Optional<PositionData> result =
        streamsAggregatedPositionHandler.persistContractAndPositionData(
            positionDBO.toKafkaPositionData());

    assertThat(result.get().getContractData().getContractId()).isEqualTo(2);
  }
}
