package de.segoy.springboottradingibkr.client.service;

import de.segoy.springboottradingibkr.client.strategybuilder.type.Leg;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class LegMapService {

    public Map<Leg, Double> mapLegs(double buyPutStrike, double sellPutStrike, double buyCallStrike, double sellCallStrike, double buyPutStrikeTwo, double sellPutStrikeTwo, double buyCallStrikeTwo, double sellCallStrikeTwo) {
        Map<Leg, Double> legs = new HashMap<>();
        if (buyPutStrike != 0) {
            legs.put(Leg.BUY_PUT_ONE, buyPutStrike);
        }
        if (sellPutStrike != 0) {
            legs.put(Leg.SELL_PUT_ONE, sellPutStrike);
        }
        if (buyCallStrike != 0) {
            legs.put(Leg.BUY_CALL_ONE, buyCallStrike);
        }
        if (sellCallStrike != 0) {
            legs.put(Leg.SELL_CALL_ONE, sellCallStrike);
        }
        if (buyPutStrikeTwo != 0) {
            legs.put(Leg.BUY_PUT_TWO, buyPutStrikeTwo);
        }
        if (sellPutStrikeTwo != 0) {
            legs.put(Leg.SELL_PUT_TWO, sellPutStrikeTwo);
        }
        if (buyCallStrikeTwo != 0) {
            legs.put(Leg.BUY_CALL_TWO, buyCallStrikeTwo);
        }
        if (sellCallStrikeTwo != 0) {
            legs.put(Leg.SELL_CALL_TWO, sellCallStrikeTwo);
        }
        return legs;
    }
}
