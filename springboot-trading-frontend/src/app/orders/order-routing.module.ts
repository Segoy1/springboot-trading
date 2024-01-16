import {NgModule} from "@angular/core";
import {OrdersComponent} from "./orders.component";
import {LoginGuard} from "../login/login.guard";
import {OrderStartComponent} from "./order-start/order-start.component";
import {OrderFormComponent} from "./order-form/order-form.component";
import {ComboLegsComponent} from "./order-form/combo-legs/combo-legs.component";
import {StrategyBuilderComponent} from "./order-form/strategy-builder/strategy-builder.component";
import {RouterModule, Routes} from "@angular/router";

const routes: Routes = [
  {path: '', component: OrdersComponent, canActivate: [LoginGuard], children: [
      {path: '', component: OrderStartComponent},
      {path: 'new', component: OrderFormComponent, children: [
          {path: '',component: ComboLegsComponent},
          {path: 'strategy', component: StrategyBuilderComponent}
        ]},
      {path: ':id', component: OrderFormComponent, children: [
          {path: '',component: ComboLegsComponent}
        ]}
    ]}
]
@NgModule({
  imports:[RouterModule.forChild(routes)],
  exports:[RouterModule]
})
export class OrderRoutingModule{}
