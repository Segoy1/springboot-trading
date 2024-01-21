import {Component} from '@angular/core';
import {AccountComponent} from "./account/account.component";
import {PositionListComponent} from "./position-list/position-list.component";

@Component({
  standalone: true,
  selector: 'app-portfolio',
  templateUrl: './portfolio.component.html',
  styleUrl: './portfolio.component.css',
  imports: [
    AccountComponent,
    PositionListComponent
  ]
})
export class PortfolioComponent{}
