import {Component, OnInit} from '@angular/core';
import {FormArray, FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {OpenOrderService} from "../service/open-order.service";
import {OrderFormValidationService} from "../service/order-form-validation.service";
import {ActivatedRoute, Params} from "@angular/router";
import {OrderSubmitService} from "../service/order-submit.service";
import {OrderIdService} from "../service/order-id.service";
import {OrderFormService} from "../service/order-form.service";

@Component({
  selector: 'app-order-form',
  templateUrl: './order-form.component.html',
  styleUrl: './order-form.component.css'
})
export class OrderFormComponent implements OnInit {

  constructor(private route: ActivatedRoute,
              private orderSubmitService: OrderSubmitService,
              private orderIdService: OrderIdService,
              private orderFormService: OrderFormService) {
  }
  getOrderForm(){
    return this.orderFormService.getForm();
  }
  onSubmit(){
    this.orderSubmitService.placeOrder(this.orderFormService.getForm().value);
  }

  ngOnInit() {
    this.orderIdService.getNextValidId();
    this.route.params
      .subscribe(
        (params: Params) => {
          this.orderFormService.id = +params['id'];
          this.orderFormService.editMode = params['id'] != null;
          this.orderFormService.initForm();
        }
      )
  }
  onCancel() {
    this.orderFormService.getForm().reset();
  }
  isEditMode(){
    return this.orderFormService.editMode;
  }
  isFormValid(){
    return this.orderFormService.getForm().valid;
  }
  isFormTouched(){
    return this.orderFormService.getForm().touched;
  }


}
