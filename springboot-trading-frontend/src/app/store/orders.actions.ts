import {createAction, props} from "@ngrx/store";
import {Order} from "../model/order.model";

export const init = createAction(
  '[Orders] Init'
);

export const add = createAction(
  '[Orders] Add',
  props<{order: Order}>()
);

export const set = createAction(
  '[Orders] Set',
  props<{orders: Order[]}>()
);
