import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';

@Injectable({
  providedIn: 'root'
})

export class HttpService {

  // private url = 'http://localhost:8080/connect';
  private url = 'http://localhost:8080/login';
  private headers:Headers;

  constructor(private http: HttpClient) {
  }

  getConnect() {

    return this.http.get(this.url);
  }
}
