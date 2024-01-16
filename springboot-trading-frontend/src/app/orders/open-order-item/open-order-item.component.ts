import {Component, Input} from '@angular/core';
import {Order} from "../../model/order.model";
import {OrderCancelService} from "../service/order-cancel.service";
import {RouterLink, RouterLinkActive} from "@angular/router";

@Component({
  standalone: true,
  selector: 'app-open-order-item',
  templateUrl: './open-order-item.component.html',
  imports: [
    RouterLink,
    RouterLinkActive
  ],
  styleUrl: './open-order-item.component.css'
})
export class OpenOrderItemComponent {
  @Input() order: Order;

  constructor(private orderCancelService: OrderCancelService) {
  }

  onCancelOrder() {
    this.orderCancelService.cancelSingleOrder(this.order.id);
  }
}
