import {Injectable} from "@angular/core";
import {Actions, createEffect, ofType} from "@ngrx/effects";
import {Store} from "@ngrx/store";
import {remove} from "./orders.actions";
import {switchMap} from "rxjs";

@Injectable()
export class CounterEffects {
  // removeItem = createEffect(
  //   () => this.actions$.pipe(
  //     ofType(remove),
  //     switchMap(()=>{
  //
  //     })
  // )
  // );

  constructor(private actions$: Actions, private store: Store) {
  }
}
