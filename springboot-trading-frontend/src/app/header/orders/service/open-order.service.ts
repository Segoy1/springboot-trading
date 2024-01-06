import {Injectable} from "@angular/core";
import {Order} from "../../model/order.model";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Position} from "../../model/position.model";
import {HttpHeaderService} from "../../shared/http-header.service";

@Injectable()
export class OpenOrderService{
  private openOrders : Order[] = [];
  private errorMessage: string;
  private url: string = 'http://localhost:8080/order/open-orders';

  constructor(private httpClient: HttpClient, private httpHeaderService: HttpHeaderService) {
  };

  initOpenOrders() {
    let fetchedOrders = [];

    this.httpClient.get<Order[]>(this.url, this.httpHeaderService.getHttpOptions()).subscribe({
      next: (openOrders) => {
          console.log(openOrders)
        openOrders.forEach((order) => {
          fetchedOrders.push(order)
        });
      },
      error: (error) => {
        this.errorMessage = error;
      }
    })
    this.openOrders = fetchedOrders;
    return fetchedOrders;
  }

  getOpenOrders() {
    return this.openOrders.slice();
  }
  getOpenOrder(id:number){
    let order1:Order;
    this.openOrders.forEach((order2)=>{
      if(order2.id===id){
        order1 = order2;
      }
    });
    return order1;
  }
}
