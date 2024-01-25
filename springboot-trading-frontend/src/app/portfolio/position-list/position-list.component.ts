import {Component, OnDestroy, OnInit} from '@angular/core';
import {Subscription} from "rxjs";
import {PositionItemComponent} from "./position-item/position-item.component";
import {NgForOf} from "@angular/common";
import {PositionsWebsocketService} from "../service/positions-websocket.service";
import {Position} from "../../model/position.model";
import {PositionsOpenCloseService} from "../service/positions-open-close.service";

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
export class PositionListComponent implements OnInit, OnDestroy {

  positions: Position[];
  portfolioSub: Subscription;


  constructor(private positionsWebsocketService: PositionsWebsocketService,
              private positionsOpenCloseService: PositionsOpenCloseService) {
  }
  ngOnInit() {
    this.portfolioSub = this.positionsWebsocketService.responseChangedSubject.subscribe(positions => {
      this.positions = positions;
    });
    this.positionsOpenCloseService.initPositions();
  }
  ngOnDestroy() {
    this.positionsOpenCloseService.cancelPositions();
    this.portfolioSub.unsubscribe();
  }

}
