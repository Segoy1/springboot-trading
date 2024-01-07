import {Injectable} from "@angular/core";
import {OpenOrderService} from "./open-order.service";
import {HttpClient} from "@angular/common/http";

@Injectable({providedIn: "root"})
export class OrderIdService {
  private url = 'http://localhost:8080/get-next-order-id';
  nextId: number;

  constructor(private http: HttpClient) {
  }

  getNextValidId() {
    this.http.get<number>(this.url).subscribe(response=>{
      this.nextId = response;
    })
  }
}
