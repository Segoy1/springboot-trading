import {Injectable} from "@angular/core";
import {Order} from "../model/order.model";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Position} from "../model/position.model";

@Injectable()
export class OpenOrderService{
  private openOrders : Order[] = [];
  private errorMessage: string;
  private url: string = 'http://localhost:8080/order/open-orders';

  constructor(private httpClient: HttpClient) {
  };

  initOpenOrders() {
    const httpOptions = {
      headers: new HttpHeaders({
        'Accept': 'application/json',
        'Content-Type': 'application/json'
      })
    };

    this.httpClient.get<Order[]>(this.url, httpOptions).subscribe({
      next: (openOrders) => {
          console.log(openOrders)
        openOrders.forEach((order) => {
          this.openOrders.push(order)
        });
      },
      error: (error) => {
        this.errorMessage = error;
      }
    })
          console.log(this.openOrders)
    return this.openOrders;
  }

  getOpenOrders() {
    return this.openOrders;
  }
}
