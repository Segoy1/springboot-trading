import {Order} from "../model/order.model";
import {createReducer, on} from "@ngrx/store";
import {add, set} from "./orders.actions";

const initialOrders: Order[] = [];

export const ordersReducer = createReducer(
  initialOrders,
  on(set, (state, action) => action.orders),
  on(add, (state, action) => ({...state, action})),

);
