import {Component, OnChanges, OnInit, SimpleChanges} from '@angular/core';
import {FormArray, FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {OpenOrderService} from "../service/open-order.service";
import {OrderFormValidationService} from "../service/order-form-validation.service";
import {ActivatedRoute, Params, Router} from "@angular/router";
import {OrderSubmitService} from "../service/order-submit.service";
import {OrderIdService} from "../service/order-id.service";
import {OrderFormService} from "../service/order-form.service";
import {error} from "@angular/compiler-cli/src/transformers/util";

@Component({
  selector: 'app-order-form',
  templateUrl: './order-form.component.html',
  styleUrl: './order-form.component.css'
})
export class OrderFormComponent implements OnInit {
  nextId:number;

  constructor(private route: ActivatedRoute,
              private orderSubmitService: OrderSubmitService,
              private orderIdService: OrderIdService,
              private orderFormService: OrderFormService,
              private router: Router) {
  }
  ngOnInit() {
    this.orderIdService.getNextValidId().subscribe({
      next:
    (id)=>{
      this.nextId=id;
    }, error:
        (error) =>{
        console.log(error);
        }});
    this.route.params
      .subscribe(
        (params: Params) => {
          this.orderFormService.id = +params['id'];
          this.orderFormService.editMode = params['id'] != null;
          this.orderFormService.initForm();
        }
      )
  }

  getOrderForm(){
    return this.orderFormService.getSimpleForm();
  }
  onSubmit(){
    this.orderSubmitService.placeOrder(this.orderFormService.getSubmitForm().getRawValue());
  }

  onStrategyBuilder(){
    this.router.navigate(['strategy'],{relativeTo:this.route});
  }
  onStandardOrder() {
    this.orderFormService.switchFromStrategyToSimple();
    this.router.navigate(['./'],{relativeTo:this.route});
  }
  onCancel() {
    this.orderFormService.getSubmitForm().reset();
  }
  isEditMode(){
    return this.orderFormService.editMode;
  }
  isStrategyMode(){
    return this.orderFormService.strategyMode;
  }
  isFormValid(){
    return this.orderFormService.getSubmitForm().valid;
  }
  isFormTouched(){
    return this.orderFormService.getSubmitForm().touched;
  }
}
