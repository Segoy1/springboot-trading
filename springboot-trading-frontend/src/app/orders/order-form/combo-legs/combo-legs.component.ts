import {Component, OnInit} from '@angular/core';
import {FormArray, ReactiveFormsModule} from "@angular/forms";
import {OrderFormService} from "../../service/order-form.service";
import {OrderSubmitService} from "../../service/order-submit.service";
import {NgForOf, NgIf} from "@angular/common";

@Component({
  standalone: true,
  selector: 'app-combo-legs',
  templateUrl: './combo-legs.component.html',
  imports: [
    ReactiveFormsModule,
    NgForOf,
    NgIf
  ],
  styleUrl: './combo-legs.component.css'
})
export class ComboLegsComponent{

  constructor(private orderFormService: OrderFormService) {
  }

  getOrderForm(){
    return this.orderFormService.getSimpleForm();
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
