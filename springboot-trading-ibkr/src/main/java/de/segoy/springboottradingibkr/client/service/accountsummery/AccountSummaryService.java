package de.segoy.springboottradingibkr.client.service.accountsummery;

import de.segoy.springboottradingdata.model.entity.AccountSummaryData;
import de.segoy.springboottradingdata.service.apiresponsecheck.noinput.NoInputListApiResponseChecker;
import de.segoy.springboottradingibkr.client.service.ApiCallerWithoutParameter;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountSummaryService {

    private final ApiCallerWithoutParameter<AccountSummaryData> accountSummaryApiCaller;
    private final NoInputListApiResponseChecker<AccountSummaryData> accountSummaryApiResponseChecker;

    public AccountSummaryService(AccountSummaryApiCaller accountSummaryApiCaller, NoInputListApiResponseChecker<AccountSummaryData> accountSummaryApiResponseChecker) {
        this.accountSummaryApiCaller = accountSummaryApiCaller;
        this.accountSummaryApiResponseChecker = accountSummaryApiResponseChecker;
    }

    public List<AccountSummaryData> getAccountSummary(){
        accountSummaryApiCaller.callApi();
        return accountSummaryApiResponseChecker.checkForApiResponseAndUpdate();
    }
}
