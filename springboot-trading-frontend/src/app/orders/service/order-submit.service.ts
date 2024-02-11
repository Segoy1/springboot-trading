import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Store} from "@ngrx/store";
import {selectStrategyMode} from "../../store/orders/modes/strategy/orders-strategy-mode.selector";
import {environment} from "../../../environments/environment";

@Injectable({providedIn: "root"})
export class OrderSubmitService {
  private orderUrl = environment.apiUrl + 'order/place-order';
  private strategyUrl = environment.apiUrl + 'order/place-strategy-order';

  constructor(private store: Store, private http: HttpClient) {
  }

  placeOrder(req: []) {
    this.store.select(selectStrategyMode).subscribe(
      strategyMode => {

        console.log("Request: ")
        console.log(req);
        this.http.post(strategyMode ? this.strategyUrl : this.orderUrl, req).subscribe();
      }
    ).unsubscribe();
  }
}
