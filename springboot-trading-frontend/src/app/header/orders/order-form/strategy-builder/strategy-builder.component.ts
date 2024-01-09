import { Component } from '@angular/core';
import {OrderFormService} from "../../service/order-form.service";
import {FormArray} from "@angular/forms";

@Component({
  selector: 'app-strategy-builder',
  templateUrl: './strategy-builder.component.html',
  styleUrl: './strategy-builder.component.css'
})
export class StrategyBuilderComponent {

  constructor(private orderFormService: OrderFormService) {
  }

  getStrategyForm(){
    return this.orderFormService.getStrategyForm();
  }
  getStrategyLegControls() {
    return (<FormArray>this.getStrategyForm().get('strategyLegs')).controls;
  }
  onDeleteStrategyLeg(index: number) {
    (<FormArray>this.getStrategyForm().get('strategyLegs')).removeAt(index);
  }

  onAddStrategyLeg() {
    (<FormArray>this.getStrategyForm().get('strategyLegs')).push(this.orderFormService.buildStrategyLeg('','',1,null));
  }
}
