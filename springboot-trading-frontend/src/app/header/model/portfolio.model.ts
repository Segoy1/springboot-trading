import {Position} from "./position.model";
import {ProfitAndLoss} from "./profit-and-loss.model";

export interface Portfolio{
  position : Position;
  profitAndLoss : ProfitAndLoss;
}
