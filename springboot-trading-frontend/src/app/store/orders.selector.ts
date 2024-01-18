import {Order} from "../model/order.model";
import {createSelector} from "@ngrx/store";

export const selectOrders = (state: { orders: Order[] }) => state.orders;

export const findOrder = (id: number) => createSelector(selectOrders, (orders) => {
  return orders.filter((order:Order)=> order.id === id).pop();
});

