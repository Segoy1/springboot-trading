package de.segoy.springboottradingdata.type;

public enum Right {
    CALL("C"),
    PUT("P");

    private final String symbol;

    private Right(String symbol){
        this.symbol = symbol;
    }

    @Override
    public String toString(){
        return symbol;
    }
}
