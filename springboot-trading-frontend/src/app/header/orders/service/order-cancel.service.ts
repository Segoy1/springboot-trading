import {Injectable} from "@angular/core";
import {Order} from "../../model/order.model";
import {HttpClient, HttpHeaders, HttpParams} from "@angular/common/http";
import {OpenOrderService} from "./open-order.service";

@Injectable({providedIn: "root"})
export class OrderCancelService {
  private errorMessage: string;
  private singleCancelUrl: string = 'http://localhost:8080/order/cancel-order';
  private allCancelUrl: string = 'http://localhost:8080/order/cancel-all-open-orders';

  constructor(private httpClient: HttpClient, private openOrderService: OpenOrderService) {
  };

  cancelSingleOrder(orderId: number) {
    let params = new HttpParams().set('id', orderId);

    this.httpClient.delete(this.singleCancelUrl,{
        params: params
      }).subscribe({
      next: () => {
        console.log('success deleting order')
      },
      error: (error) => {
        this.errorMessage = error;
      }
    });
    this.openOrderService.removeOrderById(orderId)
  }

  cancelAllOpenOrders() {

    this.httpClient.delete(this.allCancelUrl).subscribe({
      next: () => {
        console.log('success deleting order')
      },
      error: (error) => {
        this.errorMessage = error;
      }
    });
    this.openOrderService.removeAllOrders();
  }
}
