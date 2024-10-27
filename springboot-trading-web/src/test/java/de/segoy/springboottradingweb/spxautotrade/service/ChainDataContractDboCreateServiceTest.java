package de.segoy.springboottradingweb.spxautotrade.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import de.segoy.springboottradingdata.model.data.entity.ContractDbo;
import de.segoy.springboottradingdata.model.data.kafka.OptionChainData;
import de.segoy.springboottradingdata.model.data.kafka.OptionListData;
import de.segoy.springboottradingdata.model.data.kafka.OptionMarketData;
import de.segoy.springboottradingdata.model.subtype.Symbol;
import de.segoy.springboottradingibkr.client.strategybuilder.StrategyBuilderService;
import de.segoy.springboottradingdata.config.TradeRuleSettingsConfig;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ChainDataContractDboCreateServiceTest {

  @Mock StrategyBuilderService strategyBuilderService;
  @Mock TradeRuleSettingsConfig tradeRuleSettingsConfig;
  @InjectMocks private StrategyFromChainDataCreator strategyFromChainDataCreator;

  private OptionChainData testData;

  @BeforeEach
  void setUp() {
    OptionListData calls = new OptionListData();
    calls.put(100, OptionMarketData.builder().delta(0.045).build());
    calls.put(101, OptionMarketData.builder().delta(0.055).build());
    calls.put(102, OptionMarketData.builder().delta(0.090).build());
    calls.put(103, OptionMarketData.builder().delta(0.03).build());

    OptionListData puts = new OptionListData();
    puts.put(95, OptionMarketData.builder().delta(-0.042).build());
    puts.put(90, OptionMarketData.builder().delta(-0.053).build());
    puts.put(85, OptionMarketData.builder().delta(-0.090).build());
    puts.put(80, OptionMarketData.builder().delta(-0.03).build());

    testData =
        OptionChainData.builder()
            .symbol(Symbol.SPX)
            .lastTradeDate(20240920L)
            .calls(calls)
            .puts(puts)
            .build();
  }

  @Test
  void simpleNumbersTest() {
    when(strategyBuilderService.getComboLegContractData(any()))
        .thenReturn(Optional.of(ContractDbo.builder().build()));
    when(tradeRuleSettingsConfig.getDelta()).thenReturn(0.05);
    strategyFromChainDataCreator.createIronCondorContractData(testData);
  }
}
