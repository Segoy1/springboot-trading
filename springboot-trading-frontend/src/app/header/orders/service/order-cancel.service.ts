import {Injectable} from "@angular/core";
import {Order} from "../../model/order.model";
import {HttpClient, HttpHeaders} from "@angular/common/http";

@Injectable({providedIn:"root"})
export class OrderCancelService{
  private errorMessage: string;
  private singleCancelUrl: string = 'http://localhost:8080/order/cancel-order';
  private allCancelUrl: string = 'http://localhost:8080/order/cancel-all-open-orders';

  constructor(private httpClient: HttpClient) {
  };

  cancelSingleOrder(orderId:number) {

    this.httpClient.delete(this.singleCancelUrl).subscribe({
      next: () => {
        console.log('success deleting order')
      },
      error: (error) => {
        this.errorMessage = error;
      }
    })
  }
  cancelAllOpenOrders() {

    this.httpClient.delete(this.allCancelUrl).subscribe({
      next: () => {
        console.log('success deleting order')
      },
      error: (error) => {
        this.errorMessage = error;
      }
    })
  }

}
