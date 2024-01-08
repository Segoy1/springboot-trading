import {Component, OnInit} from '@angular/core';
import {FormArray, FormControl, FormGroup, Validators} from "@angular/forms";
import {OrderFormValidationService} from "../../service/order-form-validation.service";
import {OrderFormService} from "../../service/order-form.service";
import {ActivatedRoute, Params} from "@angular/router";
import {OpenOrderService} from "../../service/open-order.service";

@Component({
  selector: 'app-combo-legs',
  templateUrl: './combo-legs.component.html',
  styleUrl: './combo-legs.component.css'
})
export class ComboLegsComponent implements OnInit{
  id: number;
  editMode=false;

  constructor(private orderFormValidationService: OrderFormValidationService,
              private orderFormService: OrderFormService,
              private route: ActivatedRoute,
              private openOrderService:OpenOrderService) {

  }
  ngOnInit() {
    this.route.params
      .subscribe(
        (params: Params) => {
          this.id = +params['id'];
          this.editMode = params['id'] != null;
          this.initForm();
        }
      )
  }
  initForm(){

    if (this.editMode) {
      const order = this.openOrderService.findOpenOrderById(this.id);

      order.contractData.comboLegs.forEach((comboLeg) => {
        (<FormArray>this.orderFormService.orderSubmitForm.get('contractData.comboLegs')).push(this.buildComboLeg(comboLeg.contractId,comboLeg.ratio,comboLeg.action, comboLeg.exchange));
      })
  }
  }

  getOrderForm(){
    return this.orderFormService.orderSubmitForm;
  }

  getComboLegControls() {
    return (<FormArray>this.orderFormService.orderSubmitForm.get('contractData.comboLegs')).controls;
  }

  onDeleteComboLeg(index: number) {
    (<FormArray>this.orderFormService.orderSubmitForm.get('contractData.comboLegs')).removeAt(index);
  }

  onAddComboLeg() {
    (<FormArray>this.orderFormService.orderSubmitForm.get('contractData.comboLegs')).push(this.buildComboLeg(null, null, '', ''));
  }
  private buildComboLeg(contractId: number, ratio: number, side: string, exchange: string) {
    return new FormGroup({
      'contractId': new FormControl(contractId, Validators.required),
      'ratio': new FormControl(ratio, [Validators.required, Validators.pattern(/^[1-9]+[0-9]*$/)]),
      'side': new FormControl(side, [Validators.required, this.validSide.bind(this)]),
      'exchange': new FormControl((exchange === '' ? this.orderFormService.orderSubmitForm.get('contractData.exchange').value : exchange), Validators.required)
    })
  }
  validSide(control: FormControl): { [s: string]: boolean } {
    if (this.orderFormValidationService.getSide().indexOf(control.value) === -1) {
      return {'noValidSide': true};
    }
  }
}
