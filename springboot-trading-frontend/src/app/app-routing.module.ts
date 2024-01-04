import {RouterModule, Routes} from "@angular/router";
import {HeaderComponent} from "./header/header.component";
import {OrdersComponent} from "./header/orders/orders.component";
import {PortfolioComponent} from "./header/portfolio/portfolio.component";
import {NgModule} from "@angular/core";
import {LoginComponent} from "./header/login/login.component";
import {MarketDataComponent} from "./header/market-data/market-data.component";

const appRoutes: Routes = [
  {path: 'home', component: HeaderComponent},
  {path: 'orders', component: OrdersComponent},
  {path: 'portfolio', component: PortfolioComponent},
  {path: 'market-data', component: MarketDataComponent},
  {path: 'login', component: LoginComponent},
];

@NgModule({
  imports: [RouterModule.forRoot(appRoutes)],
  exports: [RouterModule]
})
export class AppRoutingModule{
}
