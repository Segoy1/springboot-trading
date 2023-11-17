package de.segoy.springboottradingdata.type;

public enum OrderType {
    LMT, //Limit
    LIT, //Limit if Touched
    LOC, //Limit on Close
    TRAIL, //Trailing Stop
    TRAIL_LIMIT,  //Trailing Stop Limit
    MKT, //Market
}
