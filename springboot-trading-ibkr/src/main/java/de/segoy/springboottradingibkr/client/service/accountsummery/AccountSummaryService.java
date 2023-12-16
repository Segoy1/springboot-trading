package de.segoy.springboottradingibkr.client.service.accountsummery;

import de.segoy.springboottradingdata.model.AccountSummary;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AccountSummaryService {

    private final AccountSummaryApiCaller accountSummaryApiCaller;

    public AccountSummaryService(AccountSummaryApiCaller accountSummaryApiCaller) {
        this.accountSummaryApiCaller = accountSummaryApiCaller;
    }

    public List<AccountSummary> getAccountSummary(){
        accountSummaryApiCaller.callApi();
        return new ArrayList<>();//TODO Implement
    }
}
