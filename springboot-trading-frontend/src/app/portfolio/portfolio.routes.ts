import {Routes} from "@angular/router";
import {LoginGuard} from "../login/login.guard";
import {PortfolioComponent} from "./portfolio.component";
import {MarketDataStartComponent} from "../market-data/market-data-start/market-data-start.component";
import {
  StandardMarketDataItemComponent
} from "../market-data/standard-market-data-item/standard-market-data-item.component";
import {PositionMarketDataComponent} from "./position-market-data/position-market-data.component";

export const routes: Routes = [{path: '', canActivate: [LoginGuard], component: PortfolioComponent, children:[
    {path: '', component: MarketDataStartComponent},
    {path: ':id', component: PositionMarketDataComponent}
  ]}]

