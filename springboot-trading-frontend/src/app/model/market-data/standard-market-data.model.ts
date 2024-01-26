import {BaseMarketData} from "./base-market-data.model";

export interface StandardMarketData extends BaseMarketData{
  tickerId: number;
  field: string;
  price: number;
  attrib: string;
}
