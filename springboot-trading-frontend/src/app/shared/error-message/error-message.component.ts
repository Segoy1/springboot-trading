import {Component, Input} from '@angular/core';
import {ErrorMessage} from "../../model/error-message.model";
import {ErrorMessageWebsocketService} from "./error-message-websocket.service";

@Component({
  standalone: true,
  selector: 'app-error-message',
  templateUrl: './error-message.component.html',
  styleUrl: './error-message.component.css'
})
export class ErrorMessageComponent  {
  @Input() errorMessage: ErrorMessage;

  constructor(private errorMessageWebsocketService:ErrorMessageWebsocketService) {
  }
  onClose(){
    this.errorMessageWebsocketService.remove(this.errorMessage);
  }

}
