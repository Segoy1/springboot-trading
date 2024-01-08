import {Component, OnInit} from '@angular/core';
import {FormArray} from "@angular/forms";
import {OrderFormValidationService} from "../../service/order-form-validation.service";
import {OrderFormService} from "../../service/order-form.service";

@Component({
  selector: 'app-combo-legs',
  templateUrl: './combo-legs.component.html',
  styleUrl: './combo-legs.component.css'
})
export class ComboLegsComponent{

  constructor(private orderFormValidationService: OrderFormValidationService,
              private orderFormService: OrderFormService) {
  }

  getOrderForm(){
    return this.orderFormService.getForm();
  }

  getComboLegControls() {
    return (<FormArray>this.getOrderForm().get('contractData.comboLegs')).controls;
  }

  onDeleteComboLeg(index: number) {
    (<FormArray>this.getOrderForm().get('contractData.comboLegs')).removeAt(index);
  }

  onAddComboLeg() {
    (<FormArray>this.getOrderForm().get('contractData.comboLegs')).push(this.orderFormService.buildComboLeg(null, null, '', ''));
  }
}
