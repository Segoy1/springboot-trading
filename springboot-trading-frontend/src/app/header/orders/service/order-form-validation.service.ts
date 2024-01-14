import {Injectable} from '@angular/core';
import {FormControl} from "@angular/forms";

@Injectable({providedIn:"root"})
export class OrderFormValidationService {
  private orderTypes = ['', 'MKT', 'LMT', 'STP', 'STP LMT', 'REL', 'TRAIL', 'BOX TOP', 'FIX PEGGED', 'LIT', 'LMT + MKT',
                        'LOC', 'MIT', 'MKT PRT', 'MOC', 'MTL', 'PASSV REL', 'PEG BENCH', 'PEG BEST', 'PEG MID', 'PEG MKT',
                        'PEG PRIM', 'PEG STK', 'REL + LMT', 'REL + MKT', 'SNAP MID', 'SNAP MKT', 'SNAP PRIM', 'STP PRT',
                        'TRAIL LIMIT', 'TRAIL LIT', 'TRAIL LMT + MKT', 'TRAIL MIT', 'TRAIL REL + MKT', 'VOL', 'VWAP',
                        'QUOTE', 'PPV', 'PDV', 'PMV', 'PSV'];

  private timeInForce = ['DAY', 'GTC', 'OPG', 'IOC', 'GTD', 'GTT', 'AUC', 'FOK', 'GTX', 'DTC', 'Minutes'];

  private securityTypes = ['None', 'STK', 'OPT', 'FUT', 'CONTFUT', 'CASH', 'BOND', 'CFD', 'FOP', 'WAR', 'IOPT', 'FWD',
                          'BAG', 'IND', 'BILL', 'FUND', 'FIXED', 'SLB', 'NEWS', 'CMDTY', 'BSK', 'ICU', 'ICS', 'CRYPTO'];

  private right = ['Put', 'Call', 'C', 'P', 'None'];

  private side = ['Buy', 'Sell', 'buy', 'sell','BUY','SELL'];

  getOrderTypes(){
    return this.orderTypes.slice();
  }
  getTIF(){
    return this.timeInForce.slice();
  }
  getSecurityTypes(){
    return this.securityTypes.slice();
  }
  getRight(){
    return this.right.slice();
  }
  getSide(){
    return this.side.slice();
  }
}
