import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {environment} from "../../../environments/environment";

@Injectable({providedIn: "root"})
export class OrderIdService {
  private url = environment.apiUrl+'get-next-order-id';

  constructor(private http: HttpClient) {
  }

  getNextValidId() {
    return this.http.get<number>(this.url);
  }
}
