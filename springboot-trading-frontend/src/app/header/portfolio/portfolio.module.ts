import {NgModule} from "@angular/core";
import {PositionListComponent} from "./position-list/position-list.component";
import {AccountPnlComponent} from "./account/account-pnl/account-pnl.component";
import {AccountMarginComponent} from "./account/account-margin/account-margin.component";
import {PositionItemComponent} from "./position-list/position-item/position-item.component";
import {AccountComponent} from "./account/account.component";
import {PortfolioComponent} from "./portfolio.component";
import {CommonModule} from "@angular/common";
import {LoginGuard} from "../login/login.guard";
import {RouterModule, Routes} from "@angular/router";
import {HttpClientModule} from "@angular/common/http";
import {NotAvailablePipe} from "../shared/not-available.pipe";

const routes: Routes = [{path: '', canActivate: [LoginGuard], component: PortfolioComponent}]
@NgModule({
  declarations: [
    PortfolioComponent,
    AccountComponent,
    PositionItemComponent,
    PositionListComponent,
    AccountPnlComponent,
    AccountMarginComponent,
    NotAvailablePipe
  ],
  imports:[
    CommonModule,
    HttpClientModule,
    RouterModule.forChild(routes)
  ],
  exports:[
    RouterModule
  ]
})
export class PortfolioModule{}
