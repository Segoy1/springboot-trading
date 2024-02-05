package de.segoy.springboottradingdata.kafkastreams.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RatioHelperTest {

    private RatioHelper ratioHelper = new RatioHelper();

    @Test
    void getRatioForSame() {
        RatioHelper.Ratios ratios = ratioHelper.getRatio(1, 1);

        assertEquals(1,1);

        assertEquals(1,ratios.gcd());
        assertEquals(1,ratios.received());
        assertEquals(1,ratios.aggregated());
    }
    @Test
    void getRatioForReceivedBigger() {
        RatioHelper.Ratios ratios = ratioHelper.getRatio(2, 5);

        assertEquals(1,ratios.gcd());
        assertEquals(5,ratios.received());
        assertEquals(2,ratios.aggregated());
    }
    @Test
    void getRatioForReceivedSmaller() {
        RatioHelper.Ratios ratios = ratioHelper.getRatio(10, 3);

        assertEquals(1,ratios.gcd());
        assertEquals(3,ratios.received());
        assertEquals(10,ratios.aggregated());
    }
    @Test
    void getRatioForDivisible() {
        RatioHelper.Ratios ratios = ratioHelper.getRatio(36, 6);

        assertEquals(6,ratios.gcd());
        assertEquals(1,ratios.received());
        assertEquals(6,ratios.aggregated());
    }
    @Test
    void getRatioForReceivedZero() {
        RatioHelper.Ratios ratios = ratioHelper.getRatio(36, 0);

        assertEquals(36,ratios.gcd());
        assertEquals(0,ratios.received());
        assertEquals(1,ratios.aggregated());
    }
    @Test
    void getRatioForDoubleZero() {
        RatioHelper.Ratios ratios = ratioHelper.getRatio(0, 0);

        assertEquals(0,ratios.gcd());
        assertEquals(0,ratios.received());
        assertEquals(0,ratios.aggregated());
    }
}
