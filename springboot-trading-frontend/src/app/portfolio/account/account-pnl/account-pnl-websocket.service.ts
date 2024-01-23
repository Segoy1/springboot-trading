import {Injectable} from "@angular/core";
import {AbstractWebsocketService} from "../../../shared/abstract-websocket.service";
import {ProfitAndLoss} from "../../../model/profit-and-loss.model";
import {environmentDevelopment} from "../../../../environments/environment.development";

@Injectable({providedIn: 'root'})
export class AccountPnlWebsocketService extends AbstractWebsocketService<ProfitAndLoss> {

  constructor() {
    super(environmentDevelopment.accountPnLTopic);
  }

  updateOrAdd(response: ProfitAndLoss): void {
    // Only has one Value, only list because of abstract Method
    this.response[0] = response;
  }

}
