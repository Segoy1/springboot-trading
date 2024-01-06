import {Component, OnChanges, OnInit, SimpleChanges} from '@angular/core';
import {Order} from "../model/order.model";
import {OpenOrderService} from "./service/open-order.service";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'app-orders',
  templateUrl: './orders.component.html',
  styleUrl: './orders.component.css'
})
export class OrdersComponent implements OnInit, OnChanges {

  openOrders: Order[];

  constructor(private openOrdersService: OpenOrderService, private route: ActivatedRoute, private router:Router) {
  }

  ngOnInit() {
    this.openOrders = this.openOrdersService.initOpenOrders();
  }

  ngOnChanges(changes: SimpleChanges) {
    this.openOrders = this.openOrdersService.getOpenOrders();
  }
  onOpenNewOrder(){
    this.router.navigate(['new'],{relativeTo:this.route});
  }
}
