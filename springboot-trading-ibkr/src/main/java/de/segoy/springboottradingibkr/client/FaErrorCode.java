package de.segoy.springboottradingibkr.client;

public enum FaErrorCode {

    FIVE_O_THREE(503),
    FIVE_O_FOUR(504),
    FIVE_O_FIVE(505),
    FIVE_TWENTY_TWO(522),
    TEN_ONE_HUNDRED(1100),
    NOT_AN_FA_ACCOUNT_ERROR(321),
    MKT_DEPTH_DATA_RESET(317);

    private int code;

    FaErrorCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
