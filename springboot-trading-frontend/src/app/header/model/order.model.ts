import {Contract} from "./contract.model";

export interface Order {
  id: number;
  action: string;
  totalQuantity: number;
  orderType: string;
  limitPrice: number;
  auctionPrice: number;
  timeInForce: string;
  cashQuantity: number;
  usePriceManagementAlgorithm: boolean;
  contractData: Contract;
  status: string;
}
