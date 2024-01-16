import {RouterModule, Routes} from "@angular/router";
import {HeaderComponent} from "./header/header.component";
import {NgModule} from "@angular/core";
import {MarketDataComponent} from "./header/market-data/market-data.component";

const appRoutes: Routes = [
  {path: 'home', component: HeaderComponent},
  {path: 'orders', loadChildren: () => import('./header/orders/orders.module').then(m => m.OrdersModule)},
  {path: 'portfolio', loadChildren: () => import('./header/portfolio/portfolio.module').then(m => m.PortfolioModule)},
  {path: 'market-data', component: MarketDataComponent},
  {path: 'login', loadChildren: () => import('./header/login/login.module').then(m => m.LoginModule)}
];

@NgModule({
  imports: [RouterModule.forRoot(appRoutes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
