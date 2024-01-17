import {Order} from "../model/order.model";

export const selectOrders = (state: {orders: Order[]}) => state.orders;
