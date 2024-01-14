import {Injectable} from "@angular/core";
import {Order} from "../../model/order.model";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Subject} from "rxjs";

@Injectable({providedIn: "root"})
export class OpenOrderService {
  private openOrders: Order[] = [];
  private errorMessage: string;
  private url: string = 'http://localhost:8080/order/open-orders';
  ordersChanged = new Subject<Order[]>();

  constructor(private httpClient: HttpClient) {
  };

  initOpenOrders() {
    let fetchedOrders = [];

    this.httpClient.get<Order[]>(this.url).subscribe({
      next: (openOrders) => {
        openOrders.forEach((order) => {
          fetchedOrders.push(order)
        });
      },
      error: (error) => {
        this.errorMessage = error;
      }
    })
    this.openOrders = fetchedOrders;

    this.ordersChange();
  }

  findOpenOrderById(id: number) {
    let returnOrder: Order;
    this.openOrders.forEach((listOrder) => {
      if (listOrder.id === id) {
        returnOrder = listOrder;
      }
    });
    return returnOrder;
  }

  addOrder(order: Order) {
    this.openOrders.push(order);
    this.ordersChange();
  }

  removeOrderById(id: number) {
    const order = this.findOpenOrderById(id);
    this.openOrders.splice(this.openOrders.indexOf(order), 1);
  }

  removeAllOrders() {
    this.openOrders = [];
  }

  ordersChange() {
    this.ordersChanged.next(this.openOrders);
  }

  getOpenOrders() {
    return this.openOrders.slice()
  }
}
