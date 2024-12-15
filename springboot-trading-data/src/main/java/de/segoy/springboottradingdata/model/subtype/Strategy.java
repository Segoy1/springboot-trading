package de.segoy.springboottradingdata.model.subtype;

public enum Strategy {
  IRON_CONDOR(1),
  BULL_SPREAD(2),
  BEAR_SPREAD(3),
  BUTTERFLY(4),
  SHORT_IRON_CONDOR(5),
  STRANGLE(6),
  SHORT_STRANGLE(7),
  IRON_BUTTERFLY(8),
  SHORT_IRON_BUTTERFLY(9),
  CUSTOM_STRATEGY(10);

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
