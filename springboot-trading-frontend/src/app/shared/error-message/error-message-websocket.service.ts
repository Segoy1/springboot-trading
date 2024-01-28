import {Injectable} from "@angular/core";
import {AbstractWebsocketService} from "../abstract-websocket.service";
import {ErrorMessage} from "../../model/error-message.model";
import {environment} from "../../../environments/environment.production";

@Injectable({providedIn:'root'})
export class ErrorMessageWebsocketService extends AbstractWebsocketService<ErrorMessage>{

  constructor() {
    super(environment.errorMessageTopic);
  }

  updateOrAdd(message: ErrorMessage) {
    this.response.push(message);
  }

  remove(message: ErrorMessage){
    const index = this.response.indexOf(message);
    this.response.splice(index,1);
  }

}
