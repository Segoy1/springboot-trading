import {

  Component,
  OnInit,
} from '@angular/core';
import {Order} from "../model/order.model";
import {ActivatedRoute, Router, RouterOutlet} from "@angular/router";
import {OrderCancelService} from "./service/order-cancel.service";
import {Observable, Subscription} from "rxjs";
import {OpenOrderItemComponent} from "./open-order-item/open-order-item.component";
import {AsyncPipe, NgForOf} from "@angular/common";
import {HttpClient} from "@angular/common/http";
import {Store} from "@ngrx/store";
import {set} from "../store/orders/orders.actions";
import {selectOrders} from "../store/orders/orders.selector";
import {selectStrategyMode} from "../store/orders/modes/strategy/orders-strategy-mode.selector";
import {selectEditMode} from "../store/orders/modes/edit/orders-edit-mode.selector";
import {environment} from "../../environments/environment";

@Component({
  standalone: true,
  selector: 'app-orders',
  templateUrl: './orders.component.html',
  imports: [
    OpenOrderItemComponent,
    RouterOutlet,
    NgForOf,
    AsyncPipe
  ],
  styleUrl: './orders.component.css'
})
export class OrdersComponent implements OnInit {

  openOrders: Observable<Order[]>;
  errorMessage: string;
  private url: string = environment.apiUrl+'order/open-orders';

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
