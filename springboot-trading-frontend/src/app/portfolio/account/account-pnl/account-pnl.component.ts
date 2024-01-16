import {Component, Input} from '@angular/core';
import {ProfitAndLoss} from "../../../model/profit-and-loss.model";
import {DecimalPipe} from "@angular/common";

@Component({
  standalone: true,
  selector: 'app-account-pnl',
  templateUrl: './account-pnl.component.html',
  imports: [
    DecimalPipe
  ],
  styleUrl: './account-pnl.component.css'
})
export class AccountPnlComponent {
  @Input() profitAndLoss: ProfitAndLoss;
}
