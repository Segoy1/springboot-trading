import {Component, OnInit} from '@angular/core';
import {FormArray, FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {OpenOrderService} from "../service/open-order.service";
import {OrderFormValidationService} from "./order-form-validation.service";

@Component({
  selector: 'app-order-form',
  templateUrl: './order-form.component.html',
  styleUrl: './order-form.component.css'
})
export class OrderFormComponent implements OnInit {

  orderSubmitForm: FormGroup;

  constructor(private formBuilder: FormBuilder, private openOrderService: OpenOrderService, private orderFormValidationService: OrderFormValidationService) {
  }

  ngOnInit() {
    this.initForm();
  }

  initForm() {
    let action = '';
    let totalQuantity = null;
    let orderType = '';
    let limitPrice = null;
    let timeInForce = '';
    let comboLegs = new FormArray([]);
    let contractData = new FormGroup({
      'contractId': new FormControl(''),
      'symbol': new FormControl(''),
      'securityType': new FormControl('STK', [this.validSecType.bind(this)]),
      'currency': new FormControl('USD'),
      'exchange': new FormControl('SMART'),
      'lastTradeDate': new FormControl(''),
      'strike': new FormControl(null),
      'right': new FormControl('', [this.validRight.bind(this)]),
      'tradingClass': new FormControl(''),
      'localSymbol': new FormControl(''),
      'comboLegs': comboLegs
    });


    this.orderSubmitForm = new FormGroup({
      'action': new FormControl<string>(action, Validators.required),
      'totalQuantity': new FormControl<number>(totalQuantity, Validators.required),
      'orderType': new FormControl<string>(orderType, [Validators.required, this.validOrderTypes.bind(this)]),
      'limitPrice': new FormControl<number>(limitPrice),
      'timeInForce': new FormControl<string>(timeInForce, [Validators.required, this.validTIF.bind(this)]),
      'contractData': contractData
    });
  }

  getComboLegControls() {
    return (<FormArray>this.orderSubmitForm.get('contractData.comboLegs')).controls;
  }

  onDeleteComboLeg(index: number) {

  }

  onAddComboLeg() {
    (<FormArray>this.orderSubmitForm.get('contractData.comboLegs')).push(
      new FormGroup({
        'contractId': new FormControl(null, Validators.required),
        'ratio': new FormControl(null, [Validators.required, Validators.pattern(/^[1-9]+[0-9]*$/)]),
        'side': new FormControl(null),
        'exchange': new FormControl(null)
      })
    )
  }

  validOrderTypes(control:FormControl):{[s:string]:boolean}{
    if(this.orderFormValidationService.getOrderTypes().indexOf(control.value)=== -1){
      return {'noValidOrderType': true};
    }
  }
  validTIF(control:FormControl):{[s:string]:boolean}{
    if(this.orderFormValidationService.getTIF().indexOf(control.value)=== -1){
      return {'noValidTIF': true};
    }
  }
  validRight(control:FormControl):{[s:string]:boolean}{
    if(this.orderFormValidationService.getRight().indexOf(control.value)=== -1){
      return {'noValidRight': true};
    }
  }
  validSecType(control:FormControl):{[s:string]:boolean}{
    if(this.orderFormValidationService.getSecurityTypes().indexOf(control.value)=== -1){
      return {'noValidSecType': true};
    }
  }
}
