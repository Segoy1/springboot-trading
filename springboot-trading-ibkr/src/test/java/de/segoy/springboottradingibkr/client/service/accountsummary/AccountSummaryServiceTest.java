package de.segoy.springboottradingibkr.client.service.accountsummary;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AccountSummaryServiceTest {

    @Mock
    private AccountSummaryApiCaller accountSummaryApiCaller;
    @Mock
    private AccountSummaryCancelApiCaller accountSummaryCancelApiCaller;
    private AccountSummaryService accountSummaryService;

    @BeforeEach
    void setup(){
        accountSummaryService = new AccountSummaryService(accountSummaryApiCaller,accountSummaryCancelApiCaller);
    }

    @Test
    void testCallApi(){
        this.accountSummaryService.getAccountSummary();
        verify(accountSummaryApiCaller,times(1)).callApi();
    }
    @Test
    void testCancelApi(){
        this.accountSummaryService.cancelAccountSummary();
        verify(accountSummaryCancelApiCaller,times(1)).callApi();
    }

}
