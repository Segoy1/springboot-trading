import {StandardMarketData} from "./standard-market-data.model";

export interface StandardTicker {
  tickerId: number,
  data: StandardMarketData[]
}
