import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Order} from "../../model/order.model";
import {Store} from "@ngrx/store";
import {add} from "../../store/orders/orders.actions";
import {selectStrategyMode} from "../../store/orders/modes/strategy/orders-strategy-mode.selector";
import {environmentDevelopment} from "../../../environments/environment.development";

@Injectable({providedIn: "root"})
export class OrderSubmitService {
  private orderUrl = environmentDevelopment.apiUrl + 'order/place-order';
  private strategyUrl = environmentDevelopment.apiUrl + 'order/place-strategy-order';

  constructor(private store: Store, private http: HttpClient) {
  }

  placeOrder(req: []) {
    console.log(this.store.select(selectStrategyMode));
    this.store.select(selectStrategyMode).subscribe(
      strategyMode => {

        console.log("Request: ")
        console.log(req);
        this.http.post<Order>(strategyMode ? this.strategyUrl : this.orderUrl, req).subscribe({
          next:
            response => {
              this.store.dispatch(add({order: response}));
              console.log("Response: ")
              console.log(response);
            }
          , error:
            err => {
              console.log(err);
            }
        });
      }
    ).unsubscribe();
  }
}
