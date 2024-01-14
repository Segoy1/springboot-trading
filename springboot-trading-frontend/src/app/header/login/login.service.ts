import {Injectable} from '@angular/core';
import {HttpClient, HttpErrorResponse, HttpHeaders} from '@angular/common/http';
import {error} from "@angular/compiler-cli/src/transformers/util";
import {BehaviorSubject, catchError, tap, throwError} from "rxjs";
import {User} from "./user.model";

export interface AuthResponse {
  username: string;
  token: string;
  authorities: string;
}

@Injectable()
export class LoginService {
  private loginUrl = 'http://localhost:8080/login'
  user = new BehaviorSubject<User>(null)

  constructor(private http: HttpClient) {
  }

  login(username: string, password: string) {
    return this.http.post<AuthResponse>(this.loginUrl,
      {
        username: username, password: password
      }).pipe(catchError(this.handleError), tap(responeData => {
      this.handleResponse(responeData.username, responeData.token, responeData.authorities)
    }))
  }

  autoLogin() {
    const userData: {
      username: string;
      token: string;
      authorities: string;
    } = JSON.parse(localStorage.getItem('userData'));
    if (!userData) {
      return
    }
    const loadedUser = new User(userData.username, userData.token, userData.authorities);
    this.user.next(loadedUser);
  }

  logout() {
    this.user.next(null);
    localStorage.removeItem('userData');
  }

  private handleResponse(username: string, token: string, authorities: string) {

    const user = new User(username, token, authorities);
    this.user.next(user);
    localStorage.setItem('userData', JSON.stringify(user));
  }

  private handleError(errorRes: HttpErrorResponse) {
    return throwError(errorRes);
  }
}
