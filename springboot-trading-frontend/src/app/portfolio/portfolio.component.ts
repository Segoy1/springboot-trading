import {Component} from '@angular/core';
import {AccountComponent} from "./account/account.component";
import {PositionListComponent} from "./position-list/position-list.component";
import {RouterOutlet} from "@angular/router";

@Component({
  standalone: true,
  selector: 'app-portfolio',
  templateUrl: './portfolio.component.html',
  styleUrl: './portfolio.component.css',
  imports: [
    AccountComponent,
    PositionListComponent,
    RouterOutlet
  ]
})
export class PortfolioComponent{}
