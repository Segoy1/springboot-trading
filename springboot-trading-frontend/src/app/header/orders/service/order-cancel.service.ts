import {Injectable} from "@angular/core";
import {Order} from "../../model/order.model";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {HttpHeaderService} from "../../shared/http-header.service";

@Injectable()
export class OrderCancelService{
  private orderId : number;
  private errorMessage: string;
  private url: string = 'http://localhost:8080/order/cancel-order';

  constructor(private httpClient: HttpClient, private httpHeaderService: HttpHeaderService) {
  };

  cancelOrder(orderId:number) {

    this.httpClient.delete(this.url, this.httpHeaderService.getHttpOptions()).subscribe({
      next: () => {
        console.log('success deleting order')
      },
      error: (error) => {
        this.errorMessage = error;
      }
    })
  }
}
