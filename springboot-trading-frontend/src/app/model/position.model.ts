import {Contract} from "./contract.model";

export interface Position {
  id: number;
  account: string;
  contractData: Contract;
  position: number;
  averageCost: number
}
