import {Injectable} from "@angular/core";
import {HttpClient, HttpParams} from "@angular/common/http";
import {Contract} from "../../model/contract.model";
import {environment} from "../../../environments/environment.production";
import {BehaviorSubject, map, Observable} from "rxjs";

@Injectable({providedIn: 'root'})
export class ContractDataRestService {
  private url = environment.apiUrl + 'contract';
  private comboPathSuffix = '/combo';
  private contracts: Contract[] = [];
  contractChange = new BehaviorSubject<Contract[]>(null);

  constructor(private http: HttpClient) {
  }

  requestContractData(id: number) {
    let params = new HttpParams().set('id', id);
    this.http.get<Contract>(this.url, {params: params}).subscribe(contract => {
      this.contracts.push(contract);
      this.contractChanged();
    })
  }

  requestContractDataForOrder(req: { contractData: any, strategyLegs: any[] }) {
    return this.http.post<Contract>(this.url + this.comboPathSuffix, req);
  }

  addContract(contract: Contract) {
    this.contracts.push(contract);
    this.contractChanged();
  }

  contractChanged() {
    this.contractChange.next(this.contracts);
  }

  getForId(id: number): Observable<Contract> {
    return this.contractChange.pipe(map<Contract[], Contract>(contracts => {
        if (contracts) {
          return contracts.find(contract => contract.id === id);
        }
        return null;
      })
    );
  }


}
