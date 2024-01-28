import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Params, Router, RouterOutlet} from "@angular/router";
import {OrderSubmitService} from "../service/order-submit.service";
import {OrderFormService} from "../service/order-form.service";
import {FormArray, FormGroup, ReactiveFormsModule} from "@angular/forms";
import {AsyncPipe, NgIf} from "@angular/common";
import {Store} from "@ngrx/store";
import {setEditMode} from "../../store/orders/modes/edit/orders-edit-mode.actions";
import {Observable} from "rxjs";
import {selectEditMode} from "../../store/orders/modes/edit/orders-edit-mode.selector";
import {selectStrategyMode} from "../../store/orders/modes/strategy/orders-strategy-mode.selector";
import {ContractDataRestService} from "../../market-data/service/contract-data-rest.service";
import {OrderMarketDataComponent} from "./order-market-data/order-market-data.component";
import {MarketDataService} from "../../shared/market-data/market-data.service";
import {findOrder} from "../../store/orders/orders.selector";
import {Contract} from "../../model/contract.model";

@Component({
  standalone: true,
  selector: 'app-order-form',
  templateUrl: './order-form.component.html',
  imports: [
    ReactiveFormsModule,
    RouterOutlet,
    NgIf,
    AsyncPipe,
    OrderMarketDataComponent
  ],
  styleUrl: './order-form.component.css'
})
export class OrderFormComponent implements OnInit {

  strategyMode$: Observable<boolean>;
  editMode$: Observable<boolean>;
  contract: Contract;

  constructor(private route: ActivatedRoute,
              private orderSubmitService: OrderSubmitService,
              private orderFormService: OrderFormService,
              private router: Router,
              private store: Store,
              private contractDataRestService: ContractDataRestService,
              private marketDataService: MarketDataService) {
    this.editMode$ = store.select(selectEditMode);
    this.strategyMode$ = store.select(selectStrategyMode);
  }

  ngOnInit() {

    this.route.params
      .subscribe(
        (params: Params) => {
          this.orderFormService.id = +params['id'];
          this.store.select(findOrder(+params['id'])).subscribe((order) => {
            console.log(order.contractData);
             this.contract = order.contractData;
             })
          this.store.dispatch(setEditMode({editMode: params['id'] != null}));
          this.orderFormService.initForm();
        }
      )
  }

  getOrderForm() {
    return this.orderFormService.getSimpleForm();
  }

  onSubmit() {
    let submitForm = this.getSubmitForm();
    this.orderSubmitService.placeOrder(submitForm.getRawValue());
    this.orderFormService.initForm();
  }

  onStrategyBuilder() {
    this.router.navigate(['strategy'], {relativeTo: this.route});
  }

  onStandardOrder() {
    this.orderFormService.switchFromStrategyToSimple();
    this.router.navigate(['./'], {relativeTo: this.route});
  }

  onCancel() {
    this.getSubmitForm().reset();
  }

  isFormValid() {
    return this.getSubmitForm().valid;
  }

  isFormTouched() {
    return this.getSubmitForm().touched;
  }

  getSubmitForm() {
    let submitForm: FormGroup;
    this.strategyMode$.subscribe((strategyMode) => {
      submitForm = strategyMode ? this.orderFormService.getStrategyForm() : this.orderFormService.getSimpleForm();
    })
    return submitForm;
  }

  onMarketData() {
    this.store.select(selectEditMode)
      .subscribe((isEditMode)=>
      {
        if(!isEditMode){
          this.marketDataNew();
        }
      }
    )

  }
  private marketDataNew(){
    let contractDataForm = new FormGroup({
      contractData: this.orderFormService.getSimpleForm().get('contractData'),
      strategyLegs: new FormArray<any>([])
    });
    this.strategyMode$.subscribe((strategyMode) => {
      if (strategyMode) {
        contractDataForm.controls['strategyLegs'] = <FormArray>this.orderFormService.getStrategyForm().get('strategyLegs');
      }
      this.contractDataRestService.requestContractDataForOrder(contractDataForm.getRawValue())
        .subscribe(contract => {
            console.log(contract);
            this.contract = contract
            this.contractDataRestService.addContract(contract);
          }
        );
    });
  }
}

