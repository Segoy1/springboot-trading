import {Component, Input} from '@angular/core';
import {AccountSummary} from "../../../model/account-summary.model";
import {CommonModule, NgForOf} from "@angular/common";

@Component({
  standalone: true,
  selector: 'app-account-margin',
  templateUrl: './account-margin.component.html',
  styleUrl: './account-margin.component.css',
  imports: [
    NgForOf
  ]
})
export class AccountMarginComponent {
@Input() accountSummary: AccountSummary[];
}
