import {Component, EventEmitter, Output} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {HttpService} from "./http.service";

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrl: './header.component.css'
})
export class HeaderComponent {
  @Output() headerSelectEvent: EventEmitter<string> = new EventEmitter<string>();
  collapsed: boolean = false;
  isConnected:any = false;

  constructor(private httpService:HttpService) {
  }

  onHeaderClick(category:string){
    this.headerSelectEvent.emit(category)
  }

  onConnect(connect:boolean){
    if(connect){
    this.httpService.getConnect().subscribe(
      response => {
        console.log(response)
        this.isConnected = response; },
      (error) => { console.log(error); });
    }if(!connect){
    this.httpService.getDisconnect().subscribe((data) => {
      console.log(data);
      this.isConnected=data;
    })
    }
  }

}
