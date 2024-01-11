import {Injectable} from "@angular/core";
import {ActivatedRouteSnapshot, Resolve, RouterStateSnapshot} from "@angular/router";
import {Order} from "../model/order.model";
import {OpenOrderService} from "./service/open-order.service";

@Injectable({providedIn: "root"})
export class OpenOrdersResolverService implements Resolve<Order[]> {
  constructor(private openOrderService: OpenOrderService) {
  }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
    const openOrders = this.openOrderService.getOpenOrders();
    if (openOrders.length === 0) {
      this.openOrderService.initOpenOrders();
    }
    return openOrders;

  }
}
