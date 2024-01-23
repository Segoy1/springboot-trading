import {Component, Input, OnInit} from '@angular/core';
import {ProfitAndLoss} from "../../../model/profit-and-loss.model";
import {DecimalPipe, NgIf} from "@angular/common";
import {AccountPnlWebsocketService} from "./account-pnl-websocket.service";
import {Subscription} from "rxjs";

@Component({
  standalone: true,
  selector: 'app-account-pnl',
  templateUrl: './account-pnl.component.html',
  imports: [
    DecimalPipe,
    NgIf
  ],
  styleUrl: './account-pnl.component.css'
})
export class AccountPnlComponent implements OnInit{
  profitAndLoss: ProfitAndLoss;
  profitAndLossSub: Subscription

  constructor(private accountPnlWebsocketService:AccountPnlWebsocketService){}

  ngOnInit() {
      this.profitAndLossSub = this.accountPnlWebsocketService.responseChangedSubject.subscribe(pnl=>
      {
        if(pnl){
        this.profitAndLoss = pnl[0];
        }
      })
  }

}
