import {Order} from "../../model/order.model";
import {createReducer, on} from "@ngrx/store";
import {add, remove, removeAll, set} from "./orders.actions";

const initialOrders: Order[] = [];

export const ordersReducer = createReducer(
  initialOrders,
  on(set, (state, action) => action.orders),
  on(add, (state, action) => state.concat(action.order)),
  on(remove, (state, action) => {
      return state.filter(order => order.id !== action.orderId);
    }
  ),
  on(removeAll, (state, action) => [])
);
