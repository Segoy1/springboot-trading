package de.segoy.springboottradingdata.model.subtype;

public enum Strategy {
    IRON_CONDOR(1),
    BULLSPREAD(2),
    BEARSPREAD(3),
    BUTTERFLY(4);

    final int numberValue;

    Strategy(int numberValue) {
        this.numberValue = numberValue;
    }
    /**
     * @return numberValue must remain smaller than 10 or it will break some generating logic
     */
    public int numberValue() {
        return numberValue;
    }
}
