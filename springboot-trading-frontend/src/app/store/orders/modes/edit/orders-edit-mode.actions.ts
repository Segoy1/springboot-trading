import {createAction, props} from "@ngrx/store";

export const setEditMode = createAction(
  '[OrdersMode] SetEditMode',
  props<{editMode: boolean}>()
);
