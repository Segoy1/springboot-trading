import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Params, Router, RouterOutlet} from "@angular/router";
import {OrderSubmitService} from "../service/order-submit.service";
import {OrderIdService} from "../service/order-id.service";
import {OrderFormService} from "../service/order-form.service";
import {FormGroup, ReactiveFormsModule} from "@angular/forms";
import {AsyncPipe, NgIf} from "@angular/common";
import {Store} from "@ngrx/store";
import {setEditMode} from "../../store/orders/modes/edit/orders-edit-mode.actions";
import { Observable} from "rxjs";
import {selectEditMode} from "../../store/orders/modes/edit/orders-edit-mode.selector";
import {selectStrategyMode} from "../../store/orders/modes/strategy/orders-strategy-mode.selector";

@Component({
  standalone: true,
  selector: 'app-order-form',
  templateUrl: './order-form.component.html',
  imports: [
    ReactiveFormsModule,
    RouterOutlet,
    NgIf,
    AsyncPipe
  ],
  styleUrl: './order-form.component.css'
})
export class OrderFormComponent implements OnInit {

  strategyMode$: Observable<boolean>;
  editMode$: Observable<boolean>;
  nextId:number;

  constructor(private route: ActivatedRoute,
              private orderSubmitService: OrderSubmitService,
              private orderIdService: OrderIdService,
              private orderFormService: OrderFormService,
              private router: Router,
              private store: Store) {
    this.editMode$ = store.select(selectEditMode);
    this.strategyMode$ = store.select(selectStrategyMode);
  }
  ngOnInit() {

    this.route.params
      .subscribe(
        (params: Params) => {
          this.orderFormService.id = +params['id'];
          // this.orderFormService.editMode = params['id'] != null;
          this.store.dispatch(setEditMode({editMode: params['id'] != null}));
          this.orderFormService.initForm();
        }
      )
  }

  getOrderForm(){
    return this.orderFormService.getSimpleForm();
  }
  onSubmit(){
    let submitForm = this.getSubmitForm();
    this.orderSubmitService.placeOrder(submitForm.getRawValue());
    this.orderFormService.initForm();
  }

  onStrategyBuilder(){
    this.router.navigate(['strategy'],{relativeTo:this.route});
  }
  onStandardOrder() {
    this.orderFormService.switchFromStrategyToSimple();
    this.router.navigate(['./'],{relativeTo:this.route});
  }
  onCancel() {
    this.getSubmitForm().reset();
  }
  isFormValid(){
    return this.getSubmitForm().valid;
  }
  isFormTouched(){
    return this.getSubmitForm().touched;
  }
  getSubmitForm() {
    let submitForm: FormGroup;
    this.strategyMode$.subscribe((strategyMode)=>{
      submitForm = strategyMode ? this.orderFormService.getStrategyForm() : this.orderFormService.getSimpleForm();
    })
    return submitForm;
  }
}
