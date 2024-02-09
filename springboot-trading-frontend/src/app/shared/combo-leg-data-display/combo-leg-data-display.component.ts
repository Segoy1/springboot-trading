import {Component, Input, OnDestroy, OnInit} from '@angular/core';
import {Contract} from "../../model/contract.model";
import {Subscription} from "rxjs";
import {ComboLeg} from "../../model/comboleg.model";
import {ProfitLossWebsocketService} from "../../portfolio/service/profit-loss-websocket.service";
import {ContractDataIdResolverRestService} from "../contract-data-id-resolver-rest.service";
import {NgForOf} from "@angular/common";

@Component({
  selector: 'app-combo-leg-data-display',
  standalone: true,
  imports: [
    NgForOf
  ],
  templateUrl: './combo-leg-data-display.component.html',
  styleUrl: './combo-leg-data-display.component.css'
})
export class ComboLegDataDisplayComponent implements OnInit, OnDestroy{
  @Input() comboLeg:ComboLeg;
  contract: Contract;
  contractsSub: Subscription;

  constructor(private contractDataRestService: ContractDataIdResolverRestService) {
  }

  ngOnInit() {

      this.contractsSub = this.contractDataRestService.getContractData(this.comboLeg.contractId).subscribe(contract => {
        this.contract = contract;
      });
  }

  getDisplayString() {
    let displayString = ''
    if(this.contract){
      displayString = this.contract.strike + " " + this.contract.right;
    }
    return displayString;
  }
  getDispalyRatio(){
    let displayRatio = 0;
    if(this.contract){
      displayRatio = this.comboLeg.ratio * (this.comboLeg.action==="BUY"?1:-1);
    }
    return displayRatio;
  }

  ngOnDestroy() {
    this.contractsSub.unsubscribe();
  }
}
