import {Order} from "../../model/order.model";
import {createReducer, on} from "@ngrx/store";
import {add, remove, removeAll, set, update} from "./orders.actions";

const initialOrders: Order[] = [];

export const ordersReducer = createReducer(
  initialOrders,
  on(set, (state, action) => action.orders),
  on(add, (state, action) => state.concat(action.order)),
  on(remove, (state, action) => {
      return state.filter(order => order.id !== action.orderId);
    }
  ),
  on(update, (state, action) => {
    const orders = state.filter(order => order.id !== action.order.id);
    return orders.concat(action.order);
  }),
  on(removeAll, (state, action) => [])
);
