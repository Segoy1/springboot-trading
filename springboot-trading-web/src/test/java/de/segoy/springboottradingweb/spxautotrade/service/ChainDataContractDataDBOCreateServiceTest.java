package de.segoy.springboottradingweb.spxautotrade.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import de.segoy.springboottradingdata.model.data.entity.ContractDataDBO;
import de.segoy.springboottradingdata.model.data.kafka.KafkaOptionChainData;
import de.segoy.springboottradingdata.model.data.kafka.KafkaOptionListData;
import de.segoy.springboottradingdata.model.data.kafka.KafkaOptionMarketData;
import de.segoy.springboottradingdata.model.subtype.Symbol;
import de.segoy.springboottradingibkr.client.strategybuilder.StrategyBuilderService;
import de.segoy.springboottradingweb.spxautotrade.settings.TradeRuleSettingsConfig;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ChainDataContractDataDBOCreateServiceTest {

  @Mock StrategyBuilderService strategyBuilderService;
  @Mock TradeRuleSettingsConfig tradeRuleSettingsConfig;
  @InjectMocks private ChainDataContractDataCreateService chainDataContractDataCreateService;

  private KafkaOptionChainData testData;

  @BeforeEach
  void setUp() {
    KafkaOptionListData calls = new KafkaOptionListData();
    calls.put(100, KafkaOptionMarketData.builder().delta(0.045).build());
    calls.put(101, KafkaOptionMarketData.builder().delta(0.055).build());
    calls.put(102, KafkaOptionMarketData.builder().delta(0.090).build());
    calls.put(103, KafkaOptionMarketData.builder().delta(0.03).build());

    KafkaOptionListData puts = new KafkaOptionListData();
    puts.put(95, KafkaOptionMarketData.builder().delta(-0.042).build());
    puts.put(90, KafkaOptionMarketData.builder().delta(-0.053).build());
    puts.put(85, KafkaOptionMarketData.builder().delta(-0.090).build());
    puts.put(80, KafkaOptionMarketData.builder().delta(-0.03).build());

    testData =
        KafkaOptionChainData.builder()
            .symbol(Symbol.SPX)
            .lastTradeDate(20240920L)
            .calls(calls)
            .puts(puts)
            .build();
  }

  @Test
  void simpleNumbersTest() {
    when(strategyBuilderService.getComboLegContractData(any()))
        .thenReturn(Optional.of(ContractDataDBO.builder().build()));
    when(tradeRuleSettingsConfig.getDelta()).thenReturn(0.05);
    chainDataContractDataCreateService.createIronCondorContractData(testData);
  }
}
