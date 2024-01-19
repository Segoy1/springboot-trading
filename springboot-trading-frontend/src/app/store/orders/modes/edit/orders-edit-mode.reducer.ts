import {createReducer, on} from "@ngrx/store";
import {setEditMode} from "./orders-edit-mode.actions";

const initialEditMode = false;

export const ordersEditModeReducer = createReducer(
  initialEditMode,
  on(setEditMode, (state, action)=> action.editMode)
)
