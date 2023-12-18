import {Component} from '@angular/core';
import {LoginService} from "../shared/login.service";
import {HttpClient} from "@angular/common/http";
import {Router} from "@angular/router";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {

  credentials = {username: '', password: ''};

  constructor(private loginService: LoginService, private http: HttpClient, private router: Router) {
  }

  login() {
    this.loginService.authenticate(this.credentials, () => {
      this.router.navigateByUrl('/');
    });
    return false;
  }
}
