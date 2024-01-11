import {RouterModule, Routes} from "@angular/router";
import {HeaderComponent} from "./header/header.component";
import {OrdersComponent} from "./header/orders/orders.component";
import {PortfolioComponent} from "./header/portfolio/portfolio.component";
import {NgModule} from "@angular/core";
import {LoginComponent} from "./header/login/login.component";
import {MarketDataComponent} from "./header/market-data/market-data.component";
import {OrderFormComponent} from "./header/orders/order-form/order-form.component";
import {OrderStartComponent} from "./header/orders/order-start/order-start.component";
import {StrategyBuilderComponent} from "./header/orders/order-form/strategy-builder/strategy-builder.component";
import {ComboLegsComponent} from "./header/orders/order-form/combo-legs/combo-legs.component";
import {OpenOrdersResolverService} from "./header/orders/open-orders-resolver.service";

const appRoutes: Routes = [
  {path: 'home', component: HeaderComponent},
  {path: 'orders', component: OrdersComponent, children: [
      {path: '', component: OrderStartComponent},
      {path: 'new', component: OrderFormComponent, children: [
          {path: '',component: ComboLegsComponent},
          {path: 'strategy', component: StrategyBuilderComponent}
        ]},
      {path: ':id', component: OrderFormComponent, children: [
          {path: '',component: ComboLegsComponent}
        ]}
    ]},
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
