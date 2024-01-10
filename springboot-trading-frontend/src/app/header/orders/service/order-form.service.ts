import {Injectable} from "@angular/core";
import {FormArray, FormControl, FormGroup, Validators} from "@angular/forms";
import {OrderFormValidationService} from "./order-form-validation.service";
import {OrderIdService} from "./order-id.service";
import {OpenOrderService} from "./open-order.service";

@Injectable({providedIn: "root"})
export class OrderFormService {
  private simpleOrderSubmitForm: FormGroup;
  private strategySubmitForm: FormGroup;
  strategyMode = false;
  editMode = false;
  id: number;

  constructor(private orderFormValidationService: OrderFormValidationService,
              private orderIdService: OrderIdService,
              private openOrderService: OpenOrderService) {
  }

  initForm() {
    this.strategyMode = false;
    let id = this.orderIdService.nextId;
    let action = '';
    let totalQuantity = null;
    let orderType = '';
    let limitPrice = null;
    let timeInForce = '';
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
    let comboLegs = new FormArray([]);

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
        (<FormArray>comboLegs).push(this.buildComboLeg(comboLeg.contractId, comboLeg.ratio, comboLeg.action, comboLeg.exchange));
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

    this.simpleOrderSubmitForm = new FormGroup({
      'id': new FormControl(id),
      'action': new FormControl<string>(action, Validators.required),
      'totalQuantity': new FormControl<number>(totalQuantity, Validators.required),
      'orderType': new FormControl<string>(orderType, [Validators.required, this.validOrderTypes.bind(this)]),
      'limitPrice': new FormControl<number>(limitPrice),
      'timeInForce': new FormControl<string>(timeInForce, [Validators.required, this.validTIF.bind(this)]),
      'contractData': contractData
    });
  }

  initStrategyForm() {
    this.strategyMode = true;

    this.simpleOrderSubmitForm.get('contractData.securityType').setValue('BAG');
    this.simpleOrderSubmitForm.get('contractData.securityType').disable();
    this.simpleOrderSubmitForm.get('contractData.lastTradeDate').addValidators(Validators.required);
    this.simpleOrderSubmitForm.get('contractData.right').disable();
    this.simpleOrderSubmitForm.get('contractData.strike').disable();

    let legs = new FormArray([]);
    (<FormArray>this.simpleOrderSubmitForm.get('contractData.comboLegs')).clear();

    this.strategySubmitForm = new FormGroup<any>({
      'orderData': this.simpleOrderSubmitForm,
      'strategyLegs': legs
    });
  }


  getSimpleForm() {
    return this.simpleOrderSubmitForm;
  }

  getStrategyForm() {
    return this.strategySubmitForm;
  }

  getSubmitForm() {
    return this.strategyMode ? this.strategySubmitForm : this.simpleOrderSubmitForm;
  }


  buildStrategyLeg(right: string, action: string, ratio: number, strike: number) {
    return new FormGroup({
      'right': new FormControl(right, [Validators.required, this.validRight.bind(this)]),
      'action': new FormControl(action, [Validators.required, this.validAction.bind(this)]),
      'ratio': new FormControl(ratio, [Validators.required, Validators.pattern(/^[1-9]+[0-9]*$/)]),
      'strike': new FormControl(strike, Validators.required)
    })
  }

  buildComboLeg(contractId: number, ratio: number, action: string, exchange: string) {
    return new FormGroup({
      'contractId': new FormControl(contractId, Validators.required),
      'ratio': new FormControl(ratio, [Validators.required, Validators.pattern(/^[1-9]+[0-9]*$/)]),
      'action': new FormControl(action, [Validators.required, this.validAction.bind(this)]),
      'exchange': new FormControl((exchange === '' ? this.simpleOrderSubmitForm.get('contractData.exchange').value : exchange), Validators.required)
    })
  }

  //--------Validators--------
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

  validAction(control: FormControl): { [s: string]: boolean } {
    if (this.orderFormValidationService.getSide().indexOf(control.value) === -1) {
      return {'noValidSide': true};
    }
  }
    validStrategyLegs(control:FormControl): { [s: string]: boolean } {
    if((<FormArray>this.getStrategyForm().get('strategyLegs')).length<2){
      return {'notAStrategy':true};
    }
    }
}
