import {Component, Input} from '@angular/core';
import {Order} from "../../model/order.model";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'app-open-order-item',
  templateUrl: './open-order-item.component.html',
  styleUrl: './open-order-item.component.css'
})
export class OpenOrderItemComponent {
  @Input() order: Order;

  constructor(private router:Router, private route:ActivatedRoute) {
  }

  onClick(){
  this.router.navigate([this.order.id,'edit'], {relativeTo:this.route});
  }
}
