package de.segoy.springboottradingibkr.client.service.accountsummary;

import de.segoy.springboottradingdata.model.entity.AccountSummaryData;
import de.segoy.springboottradingdata.service.apiresponsecheck.noinput.NoInputListApiResponseChecker;
import de.segoy.springboottradingibkr.client.service.ApiCallerWithoutParameter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountSummaryService {

    private final ApiCallerWithoutParameter accountSummaryApiCaller;
    private final ApiCallerWithoutParameter accountSummaryCancelApiCaller;
    private final NoInputListApiResponseChecker<AccountSummaryData> accountSummaryApiResponseChecker;

    public AccountSummaryService(@Qualifier("AccountSummaryApiCaller") AccountSummaryApiCaller accountSummaryApiCaller,
                                 @Qualifier("AccountSummaryCancelApiCaller") ApiCallerWithoutParameter accountSummaryCancelApiCaller,
                                 NoInputListApiResponseChecker<AccountSummaryData> accountSummaryApiResponseChecker) {
        this.accountSummaryApiCaller = accountSummaryApiCaller;
        this.accountSummaryCancelApiCaller = accountSummaryCancelApiCaller;
        this.accountSummaryApiResponseChecker = accountSummaryApiResponseChecker;
    }

    public List<AccountSummaryData> getAccountSummary() {
        //cancel call if an active call is still ongoing. Call is idempotent so no if clause
        accountSummaryCancelApiCaller.callApi();
        accountSummaryApiCaller.callApi();
        return accountSummaryApiResponseChecker.checkForApiResponseAndUpdate();
    }

    public void cancelAccountSummary(){
        accountSummaryCancelApiCaller.callApi();
    }
}
