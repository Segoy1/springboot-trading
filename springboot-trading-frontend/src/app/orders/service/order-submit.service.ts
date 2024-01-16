import {Injectable} from "@angular/core";
import {OpenOrderService} from "./open-order.service";
import {HttpClient} from "@angular/common/http";
import {Order} from "../../model/order.model";

@Injectable({providedIn: "root"})
export class OrderSubmitService{
  private orderUrl = 'http://localhost:8080/order/place-order';
  private strategyUrl = 'http://localhost:8080/order/place-strategy-order';
  private activeUrl= this.orderUrl;

  constructor(private openOrderService: OpenOrderService, private http: HttpClient) {
  }
  setUrlToStrategyRequest(strategy: boolean){
    this.activeUrl = strategy?this.strategyUrl:this.orderUrl;
  }

  placeOrder(req : []){
    console.log(req);
    this.http.post<Order>(this.activeUrl, req).subscribe({
      next:
      response => {
      this.openOrderService.addOrder(response);
      console.log(response);
    }
    ,error:
    err => {
      console.log(err);
      }
    });
  }
}
