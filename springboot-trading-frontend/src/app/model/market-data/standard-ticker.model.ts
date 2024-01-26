import {StandardMarketData} from "./standard-market-data.model";
import {BaseTicker} from "./base-ticker.model";

export interface StandardTicker extends BaseTicker{
  tickerId: number,
  data: StandardMarketData[]
}
