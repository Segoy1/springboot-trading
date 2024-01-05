import {Component, OnInit} from '@angular/core';
import {FormArray, FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {OpenOrderService} from "../open-order.service";

@Component({
  selector: 'app-order-form',
  templateUrl: './order-form.component.html',
  styleUrl: './order-form.component.css'
})
export class OrderFormComponent implements OnInit {

  orderSubmitForm: FormGroup;

  constructor(private formBuilder: FormBuilder, openOrderService: OpenOrderService) {
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
      'securityType': new FormControl('STK'),
      'currency': new FormControl('USD'),
      'exchange': new FormControl('SMART'),
      'lastTradeDate': new FormControl(''),
      'strike': new FormControl(null),
      'right': new FormControl(''),
      'tradingClass': new FormControl(''),
      'localSymbol': new FormControl(''),
      'comboLegs': comboLegs
    });



    this.orderSubmitForm = new FormGroup({
      'action': new FormControl<string>(action, Validators.required),
      'totalQuantity': new FormControl<number>(totalQuantity, Validators.required),
      'orderType': new FormControl<string>(orderType, Validators.required),
      'limitPrice': new FormControl<number>(limitPrice),
      'timeInForce': new FormControl<string>(timeInForce),
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
}
