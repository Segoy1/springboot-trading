import {Component, OnChanges, OnInit, SimpleChanges} from '@angular/core';
import {Order} from "../model/order.model";
import {OpenOrderService} from "./open-order.service";

@Component({
  selector: 'app-orders',
  templateUrl: './orders.component.html',
  styleUrl: './orders.component.css'
})
export class OrdersComponent implements OnInit, OnChanges {

  openOrders: Order[];

  constructor(private openOrdersService: OpenOrderService) {
  }

  ngOnInit() {
    this.openOrders = this.openOrdersService.initOpenOrders();
  }

  ngOnChanges(changes: SimpleChanges) {
    this.openOrders = this.openOrdersService.getOpenOrders();
  }
}
