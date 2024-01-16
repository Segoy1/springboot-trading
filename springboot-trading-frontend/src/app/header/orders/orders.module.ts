import {NgModule} from "@angular/core";
import {OpenOrderItemComponent} from "./open-order-item/open-order-item.component";
import {OrderFormComponent} from "./order-form/order-form.component";
import {OrderStartComponent} from "./order-start/order-start.component";
import {ComboLegsComponent} from "./order-form/combo-legs/combo-legs.component";
import {StrategyBuilderComponent} from "./order-form/strategy-builder/strategy-builder.component";
import {OrdersComponent} from "./orders.component";
import {ReactiveFormsModule} from "@angular/forms";
import {CommonModule} from "@angular/common";
import {HttpClientModule} from "@angular/common/http";
import {OrderRoutingModule} from "./order-routing.module";

@NgModule({
  declarations: [
    OrdersComponent,
    OpenOrderItemComponent,
    OrderFormComponent,
    OrderStartComponent,
    ComboLegsComponent,
    StrategyBuilderComponent,],
  imports: [
    CommonModule,
    HttpClientModule,
    ReactiveFormsModule,
    OrderRoutingModule
  ]
})
export class OrdersModule {
}
