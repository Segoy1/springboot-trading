import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Contract} from "../../model/contract.model";
import {environment} from "../../../environments/environment.production";
import {BehaviorSubject, map, Observable} from "rxjs";

@Injectable({providedIn:'root'})
export class ContractDataRestService{
  private url = environment.apiUrl+'contract';
  private contracts:Contract[] = [];
  contractChange = new BehaviorSubject<Contract[]>(null);

  constructor(private http: HttpClient){
  }

  requestContractData(id:number){
    this.http.get<Contract>(this.url+'?id='+id).subscribe(contract=>{
      this.contracts.push(contract);
      this.contractChanged();
    })
  }
  contractChanged(){
    this.contractChange.next(this.contracts);
  }
  getForId(id:number):Observable<Contract> {
    return this.contractChange.pipe(map<Contract[], Contract>(contracts => {
      if (contracts) {
        return contracts.find(contract => contract.id === id);
      }
      return null;
    }));
  }
}
