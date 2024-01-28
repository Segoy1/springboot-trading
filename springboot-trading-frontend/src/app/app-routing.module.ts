import {RouterModule, Routes} from "@angular/router";
import {NgModule} from "@angular/core";
import {HttpClientModule} from "@angular/common/http";

const appRoutes: Routes = [
  {path: 'orders', loadChildren: () => import('./orders/orders.routes').then(m => m.routes)},
  {path: 'portfolio', loadChildren: () => import('./portfolio/portfolio.routes').then(m => m.routes)},
  {path: 'market-data', loadChildren: () => import('./market-data/market-data.routes').then(m=> m.routes)},
  {path: 'login', loadChildren: () => import('./login/login.routes').then((mod)=> mod.routes)}
];

@NgModule({
  imports: [RouterModule.forRoot(appRoutes), HttpClientModule],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
