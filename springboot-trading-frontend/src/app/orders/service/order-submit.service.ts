import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Store} from "@ngrx/store";
import {selectStrategyMode} from "../../store/orders/modes/strategy/orders-strategy-mode.selector";
import {environmentDevelopment} from "../../../environments/environment.development";

@Injectable({providedIn: "root"})
export class OrderSubmitService {
  private orderUrl = environmentDevelopment.apiUrl + 'order/place-order';
  private strategyUrl = environmentDevelopment.apiUrl + 'order/place-strategy-order';

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
