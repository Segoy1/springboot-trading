import {RouterModule, Routes} from "@angular/router";
import {HeaderComponent} from "./header/header.component";
import {OrdersComponent} from "./header/orders/orders.component";
import {PortfolioComponent} from "./header/portfolio/portfolio.component";
import {NgModule} from "@angular/core";
import {LoginComponent} from "./header/login/login.component";

const appRoutes: Routes = [
  {path: 'home', component: HeaderComponent},
  {path: 'orders', component: OrdersComponent},
  {path: 'portfolio', component: PortfolioComponent},
  {path: 'login', component: LoginComponent},
];

@NgModule({
  imports: [RouterModule.forRoot(appRoutes)],
  exports: [RouterModule]
})
export class AppRoutingModule{
}
