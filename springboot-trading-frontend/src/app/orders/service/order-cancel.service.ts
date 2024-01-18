import {Injectable} from "@angular/core";
import {HttpClient, HttpParams} from "@angular/common/http";
import {Store} from "@ngrx/store";
import {findOrder, selectOrders} from "../../store/orders.selector";
import {remove, removeAll} from "../../store/orders.actions";

@Injectable({providedIn: "root"})
export class OrderCancelService {
  private errorMessage: string;
  private singleCancelUrl: string = 'http://localhost:8080/order/cancel-order';
  private allCancelUrl: string = 'http://localhost:8080/order/cancel-all-open-orders';

  constructor(private httpClient: HttpClient, private store: Store) {
  };

  cancelSingleOrder(orderId: number) {
    let params = new HttpParams().set('id', orderId);

    this.httpClient.delete(this.singleCancelUrl, {
      params: params
    }).subscribe({
      next: () => {
        console.log('success deleting order')
      },
      error: (error) => {
        this.errorMessage = error;
      }
    });
        this.store.dispatch(remove({orderId: orderId}));
        this.store.select(selectOrders).subscribe((orders)=>
        console.log(orders));
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
    this.store.dispatch(removeAll());
  }
}
