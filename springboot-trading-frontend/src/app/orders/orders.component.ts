import {

  Component,
  OnInit,
} from '@angular/core';
import {Order} from "../model/order.model";
import {ActivatedRoute, Router, RouterOutlet} from "@angular/router";
import {OrderCancelService} from "./service/order-cancel.service";
import {Observable} from "rxjs";
import {OpenOrderItemComponent} from "./open-order-item/open-order-item.component";
import {AsyncPipe, NgForOf} from "@angular/common";
import {HttpClient} from "@angular/common/http";
import {Store} from "@ngrx/store";
import {selectOrders} from "../store/orders/orders.selector";
import {environmentDevelopment} from "../../environments/environment.development";

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
  private url: string = environmentDevelopment.apiUrl+'order/open-orders';

  constructor(private http: HttpClient,
              private orderCancelService: OrderCancelService,
              private route: ActivatedRoute,
              private router: Router,
              private store: Store) {
  }

  ngOnInit() {
    this.openOrders = this.store.select(selectOrders);
    this.http.get(this.url).subscribe();
  }

  onOpenNewOrder() {
    this.router.navigate(['new'], {relativeTo: this.route});
  }

  onCancelAllOpenOrders() {
    this.orderCancelService.cancelAllOpenOrders();
  }
}
