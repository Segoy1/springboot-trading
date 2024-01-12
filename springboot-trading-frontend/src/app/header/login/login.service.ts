import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {error} from "@angular/compiler-cli/src/transformers/util";
import {BehaviorSubject} from "rxjs";

export interface Token{
  username: string;
  token: string;
  authorities: string;
}
@Injectable()
export class LoginService {
  private loginUrl = 'http://localhost:8080/login'
  token = new BehaviorSubject<Token>(null)

  constructor(private http: HttpClient) {
  }

  login(username: string, password: string) {
    this.http.post<Token>(this.loginUrl,
      {
        username: username, password: password
      })
      .subscribe({
          next:
            (res) => {
            // this.token = res
              console.log(res);
            },
          error:
            (error) => {
              console.log(error);
            }
        }
      );
  }
  // private handleResponse(token: string){
  //   const token =
  // }
}
