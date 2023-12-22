import {Component, Input} from '@angular/core';
import {Order} from "../../model/order.model";

@Component({
  selector: 'app-open-order-item',
  templateUrl: './open-order-item.component.html',
  styleUrl: './open-order-item.component.css'
})
export class OpenOrderItemComponent {
  @Input() order: Order;

  constructor() {
  }
}
