import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {environment} from "../../environments/environment";

@Injectable({
  providedIn: 'root'
})

export class ConnectionService {

  private connectUrl = environment.apiUrl+'connect';
  private disconnectUrl = environment.apiUrl+'disconnect';

  constructor(private http: HttpClient) {
  }

  getConnect() {
    this.http.get(this.connectUrl);
  }
  getDisconnect(){
    this.http.get(this.disconnectUrl);
  }
}
