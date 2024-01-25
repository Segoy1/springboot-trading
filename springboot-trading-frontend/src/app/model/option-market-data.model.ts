export interface OptionMarketData{
  tickerId: number;

  //Resolve with  TickType.getField( field):
  //10 = bid 11= ask 12=last 13=model
  field: number;
  tickAttrib: number;
  impliedVol: number;
  delta: number;
  optPrice: number;
  pvDividend: number;
  gamma: number;
  vega: number;
  theta:number;
  undPrice: number;
}
