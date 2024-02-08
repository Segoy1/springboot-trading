import {Injectable} from "@angular/core";
import {environment} from "../../../environments/environment.production";
import {HttpClient, HttpParams} from "@angular/common/http";
import {Contract} from "../../model/contract.model";

@Injectable({providedIn: 'root'})
export class ContractDataIdResolverRestService {
  private contractUrl = environment.apiUrl + 'contract/contract-id';

  constructor(private http: HttpClient) {
  }

  getContractData(id: number) {
    let params = new HttpParams().set('id', id);
    return this.http.get<Contract>(this.contractUrl, {params: params});
  }
}
