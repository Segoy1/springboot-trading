import {Injectable} from "@angular/core";
import { FormBuilder } from '@angular/forms';
import {OpenOrderService} from "./open-order.service";
import {HttpClient} from "@angular/common/http";

@Injectable({providedIn: "root"})
export class OrderSubmitService{
  private url = 'http://localhost:8080/order/place-order';

  constructor(private openOrderService: OpenOrderService, private http: HttpClient) {
  }

  placeOrder(req : []){
    console.log(req);
    this.http.post(this.url, req).subscribe(response => {
      console.log(response);
    })
  }
}
