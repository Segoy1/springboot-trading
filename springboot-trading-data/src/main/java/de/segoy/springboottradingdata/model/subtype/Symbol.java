package de.segoy.springboottradingdata.model.subtype;


public enum Symbol {
  SPX(1,5),
  AAPL(2, 2.5),
  AMZN(3, 2.5),
  NVDA(4,1),
  VIX(5,0.5),
  TLT(6,0.5),
  GC(7,5),
  CL(8, 0.25),
  NDX(9, 10),
  XOI(10, 0),
  SPXW(11,5),
  SPY(12,1);

  final int numberValue;
  final double optionInterval;

  Symbol(int numberValue, double optionInterval) {
    this.numberValue = numberValue;
    this.optionInterval = optionInterval;
  }

  /**
   * @return numberValue must remain smaller than 100 or it will break some generating logic
   */
  public int numberValue() {
    return numberValue;
  }
  public double optionInterval() {
    return optionInterval;
  }

  public static Symbol fromValue(int value) {
    for (Symbol symbol : Symbol.values()) {
      if (symbol.numberValue() == value) {
        return symbol;
      }
    }
    return null;
  }
}
