import {Component, OnInit} from '@angular/core';
import {OrderFormService} from "../../service/order-form.service";
import {FormArray, ReactiveFormsModule} from "@angular/forms";
import {OrderSubmitService} from "../../service/order-submit.service";
import {NgForOf, NgIf} from "@angular/common";

@Component({
  standalone: true,
  selector: 'app-strategy-builder',
  templateUrl: './strategy-builder.component.html',
  imports: [
    ReactiveFormsModule,
    NgForOf,
    NgIf
  ],
  styleUrl: './strategy-builder.component.css'
})
export class StrategyBuilderComponent  implements OnInit{

  constructor(private orderFormService: OrderFormService){
  }
  ngOnInit() {
    this.orderFormService.initStrategyForm();
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
