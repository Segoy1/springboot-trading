import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";

@Injectable({providedIn: "root"})
export class OrderIdService {
  private url = 'http://localhost:8080/get-next-order-id';

  constructor(private http: HttpClient) {
  }

  getNextValidId() {
    return this.http.get<number>(this.url);
  }
}
