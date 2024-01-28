import {Injectable} from "@angular/core";
import {FormArray, FormControl, FormGroup, Validators} from "@angular/forms";
import {OrderFormValidationService} from "./order-form-validation.service";
import {Store} from "@ngrx/store";
import {findOrder} from "../../store/orders/orders.selector";
import {Order} from "../../model/order.model";
import {setStrategyMode} from "../../store/orders/modes/strategy/orders-strategy-mode.actions";
import {selectEditMode} from "../../store/orders/modes/edit/orders-edit-mode.selector";
import {OrderIdService} from "./order-id.service";

@Injectable({providedIn: "root"})
export class OrderFormService {
  private simpleOrderSubmitForm: FormGroup;
  private strategySubmitForm: FormGroup;
  id: number;
  nextId: number;

  constructor(private orderFormValidationService: OrderFormValidationService,
              private store: Store,
              private orderIdService: OrderIdService) {
  }

  initForm() {
    this.store.dispatch(setStrategyMode({strategyMode: false}));

    this.store.select(selectEditMode).subscribe(editMode => {
      if (editMode) {
        this.store.select(findOrder(this.id)).subscribe((order) => {
          this.setFormValues(order);
        }).unsubscribe();
      } else {
        this.getNextId();
        this.setFormValues();
      }
    }).unsubscribe();
  }

  private setFormValues(order?: Order) {
    let comboLegs = new FormArray([]);
    if (order) {
      order.contractData.comboLegs.forEach((comboLeg) => {
        (<FormArray>comboLegs).push(this.buildComboLeg(comboLeg.contractId, comboLeg.ratio, comboLeg.action, comboLeg.exchange));
      });
    }

    let contractData = new FormGroup({
      'contractId': new FormControl(order ? order.contractData.contractId : null),
      'symbol': new FormControl(order ? order.contractData.symbol : null),
      'securityType': new FormControl(order ? order.contractData.securityType : 'STK', [this.validSecType.bind(this)]),
      'currency': new FormControl(order ? order.contractData.currency : 'USD'),
      'exchange': new FormControl(order ? order.contractData.exchange : 'SMART'),
      'lastTradeDate': new FormControl(order ? order.contractData.lastTradeDate : null),
      'strike': new FormControl(order ? order.contractData.strike : null),
      'right': new FormControl(order ? order.contractData.right : 'None', [this.validRight.bind(this)]),
      'tradingClass': new FormControl(order ? order.contractData.tradingClass : null),
      'localSymbol': new FormControl(order ? order.contractData.localSymbol : null),
      'comboLegs': comboLegs
    });
    this.simpleOrderSubmitForm = new FormGroup({
      'id': new FormControl(order ? order.id : this.nextId),
      'action': new FormControl<string>(order ? order.action : null, Validators.required),
      'totalQuantity': new FormControl<number>(order ? order.totalQuantity : null, Validators.required),
      'orderType': new FormControl<string>(order ? order.orderType : null, [Validators.required, this.validOrderTypes.bind(this)]),
      'limitPrice': new FormControl<number>(order ? order.limitPrice : null),
      'timeInForce': new FormControl<string>(order ? order.timeInForce : null, [Validators.required, this.validTIF.bind(this)]),
      'contractData': contractData
    });
  }

  initStrategyForm() {
    this.store.dispatch(setStrategyMode({strategyMode: true}));
    // this.strategyMode = true;

    this.simpleOrderSubmitForm.get('contractData.securityType').setValue('BAG');
    this.simpleOrderSubmitForm.get('contractData.securityType').disable();
    this.simpleOrderSubmitForm.get('contractData.lastTradeDate').addValidators(Validators.required);
    this.simpleOrderSubmitForm.get('contractData.right').disable();
    this.simpleOrderSubmitForm.get('contractData.right').setValue('None');
    this.simpleOrderSubmitForm.get('contractData.strike').disable();
    this.simpleOrderSubmitForm.get('contractData.strike').setValue(null);

    let legs = new FormArray([]);
    (<FormArray>this.simpleOrderSubmitForm.get('contractData.comboLegs')).clear();

    this.strategySubmitForm = new FormGroup<any>({
      'orderData': this.simpleOrderSubmitForm,
      'strategyLegs': legs
    });
  }

  switchFromStrategyToSimple() {
    this.store.dispatch(setStrategyMode({strategyMode: false}));
    // this.strategyMode = false;

    this.simpleOrderSubmitForm.get('contractData.securityType').setValue('BAG');
    this.simpleOrderSubmitForm.get('contractData.securityType').enable();
    this.simpleOrderSubmitForm.get('contractData.lastTradeDate').removeValidators(Validators.required);
    this.simpleOrderSubmitForm.get('contractData.right').enable();
    this.simpleOrderSubmitForm.get('contractData.strike').enable();
  }

  getNextId() {
    this.orderIdService.getNextValidId().subscribe({
      next:
        (id) => {
          this.nextId = id;
        }, error:
        (error) => {
          console.log(error);
        }
    });
  }


  getSimpleForm() {
    if (this.simpleOrderSubmitForm == null) {
      this.initForm();
    }
    return this.simpleOrderSubmitForm;
  }

  getStrategyForm() {
    return this.strategySubmitForm;
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

  // validStrategyLegs(control: FormControl): { [s: string]: boolean } {
  //   if ((<FormArray>this.getStrategyForm().get('strategyLegs')).length < 2) {
  //     return {'notAStrategy': true};
  //   }
  // }
}
