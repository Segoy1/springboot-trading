import {BaseMarketData} from "./base-market-data.model";

export interface BaseTicker{
  tickerId:number;
  data: BaseMarketData[];
}
