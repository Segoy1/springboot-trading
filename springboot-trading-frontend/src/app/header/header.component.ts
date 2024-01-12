import {Component, EventEmitter, Output} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {HttpService} from "./http.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrl: './header.component.css'
})
export class HeaderComponent {
  @Output() headerSelectEvent: EventEmitter<string> = new EventEmitter<string>();
  collapsed: boolean = false;
  isConnected:any = true;

  constructor(private httpService:HttpService, private router:Router) {
  }

  onHeaderClick(category:string){
    this.headerSelectEvent.emit(category)
    this.router.navigate([category]);
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
