import {Injectable} from "@angular/core";
import {Order} from "../../model/order.model";
import {HttpClient} from "@angular/common/http";
import {BehaviorSubject} from "rxjs";

@Injectable({providedIn: "root"})
export class OpenOrderService {
  private openOrders: Order[] = [];

  ordersChanged = new BehaviorSubject<Order[]>(null);


  findOpenOrderById(id: number) {
    let returnOrder: Order;
    this.openOrders.forEach((listOrder) => {
      if (listOrder.id === id) {
        returnOrder = listOrder;
      }
    });
    return returnOrder;
  }


  removeOrderById(id: number) {
    const order = this.findOpenOrderById(id);
    this.openOrders.splice(this.openOrders.indexOf(order), 1);
  }

  removeAllOrders() {
    this.openOrders = [];
  }
}
