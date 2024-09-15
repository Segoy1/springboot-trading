package de.segoy.springboottradingdata.model.subtype;

public enum Symbol {
  SPX(1),
  AAPL(2),
  AMZN(3),
  NVDA(4),
  VIX(5),
  TLT(6),
  GC(7),
  CL(8),
  NDX(9),
  XOI(10);

  final int numberValue;

  Symbol(int numberValue) {
    this.numberValue = numberValue;
  }

  public int numberValue() {
    return numberValue;
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
