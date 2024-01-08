import {Injectable} from "@angular/core";
import { FormBuilder } from '@angular/forms';
import {OpenOrderService} from "./open-order.service";
import {HttpClient} from "@angular/common/http";
import {Order} from "../../model/order.model";

@Injectable({providedIn: "root"})
export class OrderSubmitService{
  private url = 'http://localhost:8080/order/place-order';

  constructor(private openOrderService: OpenOrderService, private http: HttpClient) {
  }

  placeOrder(req : []){
    console.log(req);
    this.http.post<Order>(this.url, req).subscribe({
      next:
      response => {
      this.openOrderService.addOrder(response);
      console.log(response);
    }
    ,error:
    err => {
      console.log(err);
      }
    });
  }
}
