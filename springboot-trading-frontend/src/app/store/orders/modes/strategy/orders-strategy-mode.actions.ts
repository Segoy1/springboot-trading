import {createAction, props} from "@ngrx/store";

export const setStrategyMode = createAction(
  '[OrdersMode] SetStrategyMode',
  props<{strategyMode: boolean}>()
);
