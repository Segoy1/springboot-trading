import {ComboLeg} from "./comboleg.model";

export interface Contract {
  id: number;
  contractId: number;
  symbol: string;
  securityType: string;
  currency: string;
  exchange: string;
  lastTradeDate: string;
  strike: number;
  right: string;
  multiplier: string;
  localSymbol: string;
  tradingClass: string;
  includeExpired: boolean;
  comboLegDescription: string;
  comboLegs: ComboLeg[];
}
