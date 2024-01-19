import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Order} from "../../model/order.model";
import {Store} from "@ngrx/store";
import {add} from "../../store/orders/orders.actions";
import {selectStrategyMode} from "../../store/orders/modes/strategy/orders-strategy-mode.selector";

@Injectable({providedIn: "root"})
export class OrderSubmitService {
  private orderUrl = 'http://localhost:8080/order/place-order';
  private strategyUrl = 'http://localhost:8080/order/place-strategy-order';

  constructor(private store: Store, private http: HttpClient) {
  }

  placeOrder(req: []) {
    const activeUrl = this.store.select(selectStrategyMode) ? this.strategyUrl : this.orderUrl;
    console.log("Request: ")
    console.log(req);
    this.http.post<Order>(activeUrl, req).subscribe({
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
}
