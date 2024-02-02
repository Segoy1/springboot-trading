package de.segoy.springboottradingibkr.client.service.accountsummary;


import de.segoy.springboottradingibkr.client.service.ApiCallerWithoutParameter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountSummaryService {

    private final @Qualifier("AccountSummaryApiCaller")ApiCallerWithoutParameter accountSummaryApiCaller;
    private final @Qualifier("AccountSummaryCancelApiCaller")ApiCallerWithoutParameter accountSummaryCancelApiCaller;

    public void getAccountSummary() {
        accountSummaryApiCaller.callApi();
    }

    public void cancelAccountSummary() {
        accountSummaryCancelApiCaller.callApi();
    }
}
