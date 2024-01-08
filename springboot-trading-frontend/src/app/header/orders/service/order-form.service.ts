import {Injectable} from "@angular/core";
import {FormGroup} from "@angular/forms";

@Injectable({providedIn: "root"})
export class OrderFormService{
  orderSubmitForm : FormGroup;

  getForm(){
    return this.orderSubmitForm;
  }
}
