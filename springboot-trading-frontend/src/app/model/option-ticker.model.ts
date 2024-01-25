import {OptionMarketData} from "./option-market-data.model";

export interface OptionTicker{
  tickerId: number,
  data: OptionMarketData[]
}
