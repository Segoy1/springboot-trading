import {

  Component,
  OnDestroy,
  OnInit,
} from '@angular/core';
import {Order} from "../model/order.model";
import {OpenOrderService} from "./service/open-order.service";
import {ActivatedRoute, Router, RouterOutlet} from "@angular/router";
import {OrderCancelService} from "./service/order-cancel.service";
import {Observable, Subscription} from "rxjs";
import {OpenOrderItemComponent} from "./open-order-item/open-order-item.component";
import {NgForOf} from "@angular/common";
import {HttpClient} from "@angular/common/http";
import {Store} from "@ngrx/store";
import {set} from "../store/orders.actions";
import {selectOrders} from "../store/orders.selector";

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
export class OrdersComponent implements OnInit {

  openOrders: Observable<Order[]>;
  orderSub: Subscription;
  errorMessage: string;
  private url: string = 'http://localhost:8080/order/open-orders';

  constructor(private http: HttpClient,
              private orderCancelService: OrderCancelService,
              private route: ActivatedRoute,
              private router: Router,
              private store: Store) {
  }

  ngOnInit() {
    const fetchedOrders: Order[] = [];
    this.http.get<Order[]>(this.url).subscribe({
      next: (openOrders) => {
        openOrders.forEach((order) => {
          fetchedOrders.push(order)
        });
        this.store.dispatch(set({orders: fetchedOrders}));
        this.openOrders = this.store.select(selectOrders);
      },
      error: (error) => {
        this.errorMessage = error;
      }
    })
  }

  onOpenNewOrder() {
    this.router.navigate(['new'], {relativeTo: this.route});
  }

  onCancelAllOpenOrders() {
    this.orderCancelService.cancelAllOpenOrders();
  }
}
