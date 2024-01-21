import {Component, OnInit} from '@angular/core';
import {Subscription} from "rxjs";
import {PositionItemComponent} from "./position-item/position-item.component";
import {NgForOf} from "@angular/common";
import {PositionService} from "../service/position.service";
import {Position} from "../../model/position.model";

@Component({
  standalone: true,
  selector: 'app-position-list',
  templateUrl: './position-list.component.html',
  imports: [
    PositionItemComponent,
    NgForOf
  ],
  styleUrl: './position-list.component.css'
})
export class PositionListComponent implements OnInit {

  positions: Position[];
  portfolioSub: Subscription;


  constructor(private positionService: PositionService) {
  }
  ngOnInit() {
    this.portfolioSub = this.positionService.positionsChanged.subscribe(positions => {
      this.positions = positions;
    });
    this.positionService.initPositions()
  }

}
