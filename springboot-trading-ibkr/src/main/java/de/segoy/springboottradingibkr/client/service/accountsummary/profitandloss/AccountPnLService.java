package de.segoy.springboottradingibkr.client.service.accountsummary.profitandloss;

import de.segoy.springboottradingibkr.client.service.ApiCallerWithoutParameter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class AccountPnLService {

    private final ApiCallerWithoutParameter accountPnLApiCaller;
    private final ApiCallerWithoutParameter accountPnLCancelApiCaller;

    public AccountPnLService(@Qualifier("AccountPnLApiCaller") ApiCallerWithoutParameter accountPnLApiCaller,
                             @Qualifier("AccountPnLCancelApiCaller")ApiCallerWithoutParameter accountPnLCancelApiCaller) {
        this.accountPnLApiCaller = accountPnLApiCaller;
        this.accountPnLCancelApiCaller = accountPnLCancelApiCaller;
    }

    public void getAccountPnL() {
        accountPnLApiCaller.callApi();
    }
    public void cancelAccountPnL(){
        accountPnLCancelApiCaller.callApi();
    }
}
