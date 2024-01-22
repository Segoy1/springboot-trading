import {Injectable} from "@angular/core";
import {AbstractWebsocketService} from "../../../shared/abstract-websocket.service";
import {AccountSummary} from "../../../model/account-summary.model";


@Injectable({providedIn: 'root'})
export class AccountMarginWebsocketService extends AbstractWebsocketService<AccountSummary> {

  constructor() {
    super('accountSummary');
  }

  updateOrAdd(response: AccountSummary): void {
    const index = this.response.findIndex(inArray => inArray.tag === response.tag);
    if (index > -1) {
      this.response[index] = response;
    } else {
      this.response.push(response);
    }
  }
}
