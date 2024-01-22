import {Injectable} from "@angular/core";
import {AbstractWebsocketService} from "../../../shared/abstract-websocket.service";
import {ProfitAndLoss} from "../../../model/profit-and-loss.model";

@Injectable({providedIn: 'root'})
export class AccountPnlWebsocketService extends AbstractWebsocketService<ProfitAndLoss> {

  constructor() {
    super('accountPnL');
  }

  updateOrAdd(response: ProfitAndLoss): void {
    // Only hast one Value, only list because of abstract Method
    this.response[0] = response;
  }

}
