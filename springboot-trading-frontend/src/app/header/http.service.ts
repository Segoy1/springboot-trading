import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';

@Injectable({
  providedIn: 'root'
})

export class HttpService {

  private connectUrl = 'http://localhost:8080/connect';
  private disconnectUrl = 'http://localhost:8080/disconnect';
  private headers:Headers;

  constructor(private http: HttpClient) {
  }

  getConnect() {
    return this.http.get(this.connectUrl);
  }
  getDisconnect(){
    return this.http.get(this.disconnectUrl);
  }
}
