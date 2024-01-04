import { Component } from '@angular/core';
import {FormBuilder} from "@angular/forms";
import {OpenOrderService} from "../open-order.service";

@Component({
  selector: 'app-order-form',
  templateUrl: './order-form.component.html',
  styleUrl: './order-form.component.css'
})
export class OrderFormComponent {

  orderSubmitForm = this.formBuilder.group({
  });

  constructor(private formBuilder: FormBuilder, openOrderService: OpenOrderService) {  }
}
