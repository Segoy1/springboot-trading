import {createReducer, on} from "@ngrx/store";
import {setStrategyMode} from "./orders-strategy-mode.actions";


const initialMode = false;

export const ordersStrategyModeReducer = createReducer(
  initialMode,
  on(setStrategyMode, (state, action) => action.strategyMode)
)
