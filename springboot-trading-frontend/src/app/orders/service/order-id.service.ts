import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {environmentDevelopment} from "../../../environments/environment.development";

@Injectable({providedIn: "root"})
export class OrderIdService {
  private url = environmentDevelopment.apiUrl+'get-next-order-id';

  constructor(private http: HttpClient) {
  }

  getNextValidId() {
    return this.http.get<number>(this.url);
  }
}
