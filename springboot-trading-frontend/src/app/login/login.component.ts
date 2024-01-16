import {Component} from '@angular/core';
import {LoginService} from "./login.service";
import {HttpClient, HttpClientModule} from "@angular/common/http";
import {Router} from "@angular/router";
import {FormsModule, NgForm} from "@angular/forms";
import {LoadingSpinnerComponent} from "../shared/loading-spinner/loading-spinner.component";
import {CommonModule} from "@angular/common";

@Component({
  standalone: true,
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.css',
  imports: [
    LoadingSpinnerComponent,
    CommonModule,
    FormsModule]
})
export class LoginComponent {

  isLoading = false;
  error: string = null;

  constructor(private loginService: LoginService, private http: HttpClient, private router: Router) {
  }

  login(form: NgForm) {
    if (!form.valid) {
      return
    }
    const username = form.value.username;
    const password = form.value.password;
    this.isLoading = true;

    this.loginService.login(username, password).subscribe({
      next:
        (resData) => {
          console.log(resData);
          this.isLoading = false;
          this.router.navigate(['./portfolio']);
        }
      , error:
        (errorMessage) => {
          this.error = errorMessage;
          this.isLoading = false;
          console.log(this.error);
        }
    });
    form.resetForm();

  }
}
