package de.segoy.springboottradingibkr.client.service.accountsummary.profitandloss;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AccountPnLServiceTest {

    @Mock
    private AccountPnLApiCaller accountPnLApiCaller;
    @Mock
    private AccountPnLCancelApiCaller accountPnLCancelApiCaller;
    private AccountPnLService accountPnLService;

    @BeforeEach
    void setup(){
        accountPnLService = new AccountPnLService(accountPnLApiCaller,accountPnLCancelApiCaller);
    }

    @Test
    void testCallApi(){
        accountPnLService.getAccountPnL();
        verify(accountPnLApiCaller,times(1)).callApi();
    }
    @Test
    void testCancelApi(){
        accountPnLService.cancelAccountPnL();
        verify(accountPnLCancelApiCaller,times(1)).callApi();
    }
}
