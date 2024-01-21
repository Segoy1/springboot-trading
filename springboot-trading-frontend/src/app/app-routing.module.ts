import {RouterModule, Routes} from "@angular/router";
import {NgModule} from "@angular/core";
import {MarketDataComponent} from "./market-data/market-data.component";
import {HttpClientModule} from "@angular/common/http";

const appRoutes: Routes = [
  {path: 'orders', loadChildren: () => import('./orders/orders.routes').then(m => m.routes)},
  {path: 'portfolio', loadChildren: () => import('./portfolio/portfolio.routes').then(m => m.routes)},
  {path: 'market-data', component: MarketDataComponent},
  {path: 'login', loadChildren: () => import('./login/login.routes').then((mod)=> mod.routes)}
];

@NgModule({
  imports: [RouterModule.forRoot(appRoutes), HttpClientModule],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
