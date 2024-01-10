import {Component, OnInit} from '@angular/core';
import {OrderFormService} from "../../service/order-form.service";
import {FormArray} from "@angular/forms";
import {OrderSubmitService} from "../../service/order-submit.service";

@Component({
  selector: 'app-strategy-builder',
  templateUrl: './strategy-builder.component.html',
  styleUrl: './strategy-builder.component.css'
})
export class StrategyBuilderComponent  implements OnInit{

  constructor(private orderFormService: OrderFormService,
              private orderSubmitService: OrderSubmitService){
  }
  ngOnInit() {
    this.orderFormService.initStrategyForm();
    this.orderSubmitService.setUrlToStrategyRequest(true);
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
