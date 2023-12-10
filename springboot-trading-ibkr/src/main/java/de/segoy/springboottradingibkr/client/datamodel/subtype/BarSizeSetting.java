package de.segoy.springboottradingibkr.client.datamodel.subtype;

public enum BarSizeSetting {
    // valid: 1/5/10/15/30 secs
    // 1/2/3/5/10/15/20/30 mins
    // 1/2/3/4/8 hours
    // 1 day 2 week 1 month
    ONE_SEC("1 secs"),
    FIVE_SECS("5 secs"),
    TEN_SECS("10 secs"),
    FIFTEEN_SECS("15 secs"),
    THIRTY_SECS("30 secs"),
    ONE_MIN("1 mins"),
    TWO_MIN("2 mins"),
    THREE_MIN("3 mins"),
    FIVE_MINS("5 mins"),
    TEN_MINS("10 mins"),
    FIFTEEN_MINS("15 mins"),
    TWENTY_MINS("20 mins"),
    THIRTY_MINS("30 mins"),
    ONE_HOUR("1 hour"),
    TWO_HOURS("2 hours"),
    THREE_HOURS("3 hours"),
    FOUR_HOURS("4 hours"),
    EIGHT_HOURS("8 hours"),
    ONE_DAY("1 day"),
    ONE_WEEK("1 W"),
    ONE_MONTH("1 M"),
    INVALID("");


    private final String value;

    BarSizeSetting(String value){
        this.value = value;
    }

    public String getValue(){
        return value;
    }

    public static BarSizeSetting get(String apiString) {
        for( BarSizeSetting type : values() ) {
            if( type.value.equalsIgnoreCase(apiString) ) {
                return type;
            }
        }
        return INVALID;
    }
}
