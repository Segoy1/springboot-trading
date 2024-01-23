import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {environmentDevelopment} from "../../environments/environment.development";

@Injectable({
  providedIn: 'root'
})

export class ConnectionService {

  private connectUrl = environmentDevelopment.apiUrl+'connect';
  private disconnectUrl = environmentDevelopment.apiUrl+'disconnect';

  constructor(private http: HttpClient) {
  }

  getConnect() {
    return this.http.get(this.connectUrl);
  }
  getDisconnect(){
    return this.http.get(this.disconnectUrl);
  }
}
