import {

  Component,
  OnDestroy,
  OnInit,
} from '@angular/core';
import {Order} from "../model/order.model";
import {OpenOrderService} from "./service/open-order.service";
import {ActivatedRoute, Router, RouterOutlet} from "@angular/router";
import {OrderCancelService} from "./service/order-cancel.service";
import {Subscription} from "rxjs";
import {OpenOrderItemComponent} from "./open-order-item/open-order-item.component";
import {NgForOf} from "@angular/common";

@Component({
  standalone: true,
  selector: 'app-orders',
  templateUrl: './orders.component.html',
  imports: [
    OpenOrderItemComponent,
    RouterOutlet,
    NgForOf
  ],
  styleUrl: './orders.component.css'
})
export class OrdersComponent implements OnInit, OnDestroy {

  openOrders: Order[];
  orderSub: Subscription;

  constructor(private openOrdersService: OpenOrderService,
              private orderCancelService: OrderCancelService,
              private route: ActivatedRoute,
              private router: Router) {
  }

  ngOnInit() {
    this.orderSub = this.openOrdersService.ordersChanged.subscribe(
      (orders: Order[]) => {
        this.openOrders = orders;
      }
    );
    this.openOrdersService.initOpenOrders();
  }

  ngOnDestroy() {
    this.orderSub.unsubscribe();
  }

  onOpenNewOrder() {
    this.router.navigate(['new'], {relativeTo: this.route});
  }

  onCancelAllOpenOrders() {
    this.orderCancelService.cancelAllOpenOrders();
  }
}
