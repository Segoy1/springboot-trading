import {Routes} from "@angular/router";
import {LoginGuard} from "../login/login.guard";
import {PortfolioComponent} from "./portfolio.component";

export const routes: Routes = [{path: '', canActivate: [LoginGuard], component: PortfolioComponent}]

