import {Injectable} from "@angular/core";
import {AbstractWebsocketService} from "../../shared/abstract-websocket.service";
import {Order} from "../../model/order.model";
import {environment} from "../../../environments/environment.production";
import {Store} from "@ngrx/store";
import {findOrder} from "../../store/orders/orders.selector";
import {add, update} from "../../store/orders/orders.actions";

@Injectable({providedIn:'root'})
export class OpenOrdersWebsocketService extends AbstractWebsocketService<Order>{


  constructor(private store: Store){
    super(environment.ordersTopic);
  }
  updateOrAdd(response: Order): void {
    if(this.store.select(findOrder(response.id))){
      this.store.dispatch(update({order:response}));
    }else{
      this.store.dispatch(add({order:response}));
    }
  }

}
