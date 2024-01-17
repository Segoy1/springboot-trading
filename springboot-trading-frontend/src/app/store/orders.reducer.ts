import {Order} from "../model/order.model";
import {createReducer} from "@ngrx/store";

const initialOrders: Order[] = [];

export const ordersReducer = createReducer(
  initialOrders,

);
