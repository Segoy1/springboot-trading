import {Component, Input} from '@angular/core';
import {Order} from "../../model/order.model";
import {ActivatedRoute, Router} from "@angular/router";
import {OrderCancelService} from "../service/order-cancel.service";

@Component({
  selector: 'app-open-order-item',
  templateUrl: './open-order-item.component.html',
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
