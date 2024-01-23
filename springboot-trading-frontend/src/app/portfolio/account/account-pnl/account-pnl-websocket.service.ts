import {Injectable} from "@angular/core";
import {AbstractWebsocketService} from "../../../shared/abstract-websocket.service";
import {ProfitAndLoss} from "../../../model/profit-and-loss.model";
import {environment} from "../../../../environments/environment";

@Injectable({providedIn: 'root'})
export class AccountPnlWebsocketService extends AbstractWebsocketService<ProfitAndLoss> {

  constructor() {
    super(environment.accountPnLTopic);
  }

  updateOrAdd(response: ProfitAndLoss): void {
    // Only has one Value, only list because of abstract Method
    this.response[0] = response;
  }

}
