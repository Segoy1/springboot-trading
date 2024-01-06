import {HttpHeaders} from "@angular/common/http";
import {Injectable} from "@angular/core";

@Injectable()
export class HttpHeaderService {

  getHttpOptions() {
    return {
      headers: new HttpHeaders({
        'Accept': 'application/json',
        'Content-Type': 'application/json'
      })
    };
  }
}
