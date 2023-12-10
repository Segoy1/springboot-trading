package de.segoy.springboottradingibkr.client.service;

import com.ib.client.Types;
import de.segoy.springboottradingdata.model.Leg;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LegMapService {


    public List<Leg> mapLegs(double buyPutStrike, double sellPutStrike, double buyCallStrike, double sellCallStrike, double buyPutStrikeTwo, double sellPutStrikeTwo, double buyCallStrikeTwo, double sellCallStrikeTwo) {
        List<Leg> legs = new ArrayList<>();
        if (buyPutStrike != 0) {
            legs.add(Leg.builder().action(Types.Action.BUY).right(Types.Right.Put).strike(buyPutStrike).ratio(1).build());
        }
        if (sellPutStrike != 0) {
            legs.add(Leg.builder().action(Types.Action.SELL).right(Types.Right.Put).strike(sellPutStrike).ratio(1).build());
        }
        if (buyCallStrike != 0) {
            legs.add(Leg.builder().action(Types.Action.BUY).right(Types.Right.Call).strike(buyCallStrike).ratio(1).build());
        }
        if (sellCallStrike != 0) {
            legs.add(Leg.builder().action(Types.Action.SELL).right(Types.Right.Call).strike(sellCallStrike).ratio(1).build());
        }
        if (buyPutStrike != 0) {
            legs.add(Leg.builder().action(Types.Action.BUY).right(Types.Right.Put).strike(buyPutStrikeTwo).ratio(2).build());
        }
        if (sellPutStrike != 0) {
            legs.add(Leg.builder().action(Types.Action.SELL).right(Types.Right.Put).strike(sellPutStrikeTwo).ratio(2).build());
        }
        if (buyCallStrike != 0) {
            legs.add(Leg.builder().action(Types.Action.BUY).right(Types.Right.Call).strike(buyCallStrikeTwo).ratio(2).build());
        }
        if (sellCallStrike != 0) {
            legs.add(Leg.builder().action(Types.Action.SELL).right(Types.Right.Call).strike(sellCallStrikeTwo).ratio(2).build());
        }
        return legs;
    }
}
