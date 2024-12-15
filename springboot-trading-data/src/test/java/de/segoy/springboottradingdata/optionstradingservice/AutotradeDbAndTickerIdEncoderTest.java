package de.segoy.springboottradingdata.optionstradingservice;

import de.segoy.springboottradingdata.model.subtype.Strategy;
import de.segoy.springboottradingdata.model.subtype.Symbol;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AutotradeDbAndTickerIdEncoderTest {

  @Mock private LastTradeDateBuilder lastTradeDateBuilder;
  @InjectMocks private AutotradeDbAndTickerIdEncoder autotradeDbAndTickerIdEncoder;

  @Test
  void test_generateIntForLastTradeDateBySymbolAndStrategy_ForIronCondorAndSPX() {
    when(lastTradeDateBuilder.getDateLongFromToday()).thenReturn(20251213L);

    assertThat(
            autotradeDbAndTickerIdEncoder.generateLongForTodayBySymbolAndStrategy(
                Symbol.SPX, Strategy.IRON_CONDOR))
        .isEqualTo(30351213);
  }
    @Test
    void test_generateIntForLastTradeDateBySymbolAndStrategy_ForShortIronCondorAndSPX() {
        when(lastTradeDateBuilder.getDateLongFromToday()).thenReturn(20251213L);

        assertThat(
                autotradeDbAndTickerIdEncoder.generateLongForTodayBySymbolAndStrategy(
                        Symbol.SPX, Strategy.SHORT_IRON_CONDOR))
                .isEqualTo(30751213);
    }
    @Test
    void test_generateIntForLastTradeDateBySymbolAndStrategy_ForIronCondorAndAAPL() {
        when(lastTradeDateBuilder.getDateLongFromToday()).thenReturn(20251213L);

        assertThat(
                autotradeDbAndTickerIdEncoder.generateLongForTodayBySymbolAndStrategy(
                        Symbol.AAPL, Strategy.IRON_CONDOR))
                .isEqualTo(40351213);
    }

    @Test
    void test_generateLongIdForLastTradeDateAndSymbold_ForIronCondorAndSPX() {

        assertThat(
                autotradeDbAndTickerIdEncoder.generateLongIdForLastTradeDateAndSymbol(20251213L,
                        Symbol.SPX))
                .isEqualTo(30251213);
    }
}
