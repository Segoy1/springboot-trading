package de.segoy.springboottradingibkr.client.service.accountsummary;


import de.segoy.springboottradingibkr.client.service.ApiCallerWithoutParameter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class AccountSummaryService {

    private final ApiCallerWithoutParameter accountSummaryApiCaller;
    private final ApiCallerWithoutParameter accountSummaryCancelApiCaller;

    public AccountSummaryService(@Qualifier("AccountSummaryApiCaller") AccountSummaryApiCaller accountSummaryApiCaller,
                                 @Qualifier("AccountSummaryCancelApiCaller") ApiCallerWithoutParameter accountSummaryCancelApiCaller
                                 ) {
        this.accountSummaryApiCaller = accountSummaryApiCaller;
        this.accountSummaryCancelApiCaller = accountSummaryCancelApiCaller;
    }

    public void getAccountSummary() {
        accountSummaryApiCaller.callApi();
    }

    public void cancelAccountSummary() {
        accountSummaryCancelApiCaller.callApi();
    }
}
