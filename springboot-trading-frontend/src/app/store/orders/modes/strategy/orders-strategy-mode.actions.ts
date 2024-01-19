import {createAction, props} from "@ngrx/store";
import {Order} from "../../../../model/order.model";

export const setStrategyMode = createAction(
  '[OrdersMode] SetStrategyMode',
  props<{strategyMode: boolean}>()
);
