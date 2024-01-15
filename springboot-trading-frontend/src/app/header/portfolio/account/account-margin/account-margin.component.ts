import {Component, Input} from '@angular/core';
import {AccountSummary} from "../../../model/account-summary.model";

@Component({
  selector: 'app-account-margin',
  templateUrl: './account-margin.component.html',
  styleUrl: './account-margin.component.css'
})
export class AccountMarginComponent {
@Input() accountSummary: AccountSummary[];
}
