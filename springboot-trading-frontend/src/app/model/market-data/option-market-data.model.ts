import {BaseMarketData} from "./base-market-data.model";

export interface OptionMarketData extends BaseMarketData{
  tickerId: number;

  //Resolve with  TickType.getField( field):
  //10 = bid 11= ask 12=last 13=model
  field: string;
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
