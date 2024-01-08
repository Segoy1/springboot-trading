import {Component, OnInit} from '@angular/core';
import {FormArray, FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {OpenOrderService} from "../service/open-order.service";
import {OrderFormValidationService} from "../service/order-form-validation.service";
import {ActivatedRoute, Params} from "@angular/router";
import {OrderSubmitService} from "../service/order-submit.service";
import {OrderIdService} from "../service/order-id.service";

@Component({
  selector: 'app-order-form',
  templateUrl: './order-form.component.html',
  styleUrl: './order-form.component.css'
})
export class OrderFormComponent implements OnInit {

  id: number;
  editMode = false;
  orderSubmitForm: FormGroup;

  constructor(private route: ActivatedRoute,
              private orderFormValidationService: OrderFormValidationService,
              private openOrderService: OpenOrderService,
              private orderSubmitService: OrderSubmitService,
              private orderIdService: OrderIdService) {
  }
  onSubmit(){
    this.orderSubmitService.placeOrder(this.orderSubmitForm.value);
  }

  ngOnInit() {
    this.orderIdService.getNextValidId();
    this.route.params
      .subscribe(
        (params: Params) => {
          this.id = +params['id'];
          this.editMode = params['id'] != null;
          this.initForm();
        }
      )
  }

  initForm() {
    let id = this.orderIdService.nextId;
    let action = '';
    let totalQuantity = null;
    let orderType = '';
    let limitPrice = null;
    let timeInForce = '';
    let comboLegs = new FormArray([]);
    let contractId = null;
    let symbol = '';
    let securityType = 'STK';
    let currency = 'USD';
    let exchange = 'SMART';
    let lastTradeDate = '';
    let strike = null;
    let right = 'None';
    let tradingClass = '';
    let localSymbol = '';

    if (this.editMode) {
      const order = this.openOrderService.findOpenOrderById(this.id);
      id = order.id
      action = order.action;
      totalQuantity = order.totalQuantity;
      orderType = order.orderType;
      limitPrice = order.limitPrice;
      timeInForce = order.timeInForce;
      contractId = order.contractData.contractId;
      symbol = order.contractData.symbol;
      currency = order.contractData.currency;
      exchange = order.contractData.exchange;
      lastTradeDate = order.contractData.lastTradeDate;
      strike = order.contractData.strike;
      right = order.contractData.right;
      tradingClass = order.contractData.tradingClass;
      localSymbol = order.contractData.localSymbol;

      order.contractData.comboLegs.forEach((comboLeg) => {
        comboLegs.push(this.buildComboLeg(comboLeg.contractId,comboLeg.ratio,comboLeg.action, comboLeg.exchange));
      })
    }

    let contractData = new FormGroup({
      'contractId': new FormControl(contractId),
      'symbol': new FormControl(symbol),
      'securityType': new FormControl(securityType, [this.validSecType.bind(this)]),
      'currency': new FormControl(currency),
      'exchange': new FormControl(exchange),
      'lastTradeDate': new FormControl(lastTradeDate),
      'strike': new FormControl(strike),
      'right': new FormControl(right, [this.validRight.bind(this)]),
      'tradingClass': new FormControl(tradingClass),
      'localSymbol': new FormControl(localSymbol),
      'comboLegs': comboLegs
    });

    this.orderSubmitForm = new FormGroup({
      'id': new FormControl(id),
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
    (<FormArray>this.orderSubmitForm.get('contractData.comboLegs')).removeAt(index);
  }

  onAddComboLeg() {
    (<FormArray>this.orderSubmitForm.get('contractData.comboLegs')).push(this.buildComboLeg(null, null, '', ''));
  }

  private buildComboLeg(contractId: number, ratio: number, side: string, exchange: string) {
    return new FormGroup({
        'contractId': new FormControl(contractId, Validators.required),
        'ratio': new FormControl(ratio, [Validators.required, Validators.pattern(/^[1-9]+[0-9]*$/)]),
        'side': new FormControl(side, [Validators.required, this.validSide.bind(this)]),
        'exchange': new FormControl((exchange === '' ? this.orderSubmitForm.get('contractData.exchange').value : exchange), Validators.required)
      })
  }

  onCancel() {
    this.orderSubmitForm.reset();
  }

  validOrderTypes(control: FormControl): { [s: string]: boolean } {
    if (this.orderFormValidationService.getOrderTypes().indexOf(control.value) === -1) {
      return {'noValidOrderType': true};
    }
  }

  validTIF(control: FormControl): { [s: string]: boolean } {
    if (this.orderFormValidationService.getTIF().indexOf(control.value) === -1) {
      return {'noValidTIF': true};
    }
  }

  validRight(control: FormControl): { [s: string]: boolean } {
    if (this.orderFormValidationService.getRight().indexOf(control.value) === -1) {
      return {'noValidRight': true};
    }
  }

  validSecType(control: FormControl): { [s: string]: boolean } {
    if (this.orderFormValidationService.getSecurityTypes().indexOf(control.value) === -1) {
      return {'noValidSecType': true};
    }
  }

  validSide(control: FormControl): { [s: string]: boolean } {
    if (this.orderFormValidationService.getSide().indexOf(control.value) === -1) {
      return {'noValidSide': true};
    }
  }
}
