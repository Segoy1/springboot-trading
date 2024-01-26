import {OptionMarketData} from "./option-market-data.model";
import {BaseTicker} from "./base-ticker.model";

export interface OptionTicker extends BaseTicker{
  tickerId: number,
  data: OptionMarketData[]
}
