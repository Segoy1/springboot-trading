import {Injectable} from "@angular/core";
import {OpenOrderService} from "./open-order.service";
import {HttpClient} from "@angular/common/http";
import {Subject} from "rxjs";

@Injectable({providedIn: "root"})
export class OrderIdService {
  private url = 'http://localhost:8080/get-next-order-id';

  constructor(private http: HttpClient) {
  }

  getNextValidId() {
    return this.http.get<number>(this.url);
  }
}
