package de.segoy.springboottradingibkr.client.strategybuilder.type;

import com.ib.client.Types;
import lombok.Getter;

public enum Leg {
    BUY_PUT_ONE(Types.Right.Put, Types.Action.BUY, 1),
    BUY_CALL_ONE(Types.Right.Call, Types.Action.BUY, 1),
    SELL_PUT_ONE(Types.Right.Put, Types.Action.SELL, 1),
    SELL_CALL_ONE(Types.Right.Call, Types.Action.SELL, 1);

    @Getter
    private final Types.Right right;
    @Getter
    private final Types.Action action;
    @Getter
    private final Integer ratio;

    Leg(Types.Right right, Types.Action action, Integer ratio) {
        this.right = right;
        this.action = action;
        this.ratio = ratio;
    }
}
