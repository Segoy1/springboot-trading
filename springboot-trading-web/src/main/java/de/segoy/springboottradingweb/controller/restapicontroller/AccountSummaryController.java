package de.segoy.springboottradingweb.controller.restapicontroller;

import de.segoy.springboottradingibkr.client.service.accountsummary.AccountSummaryService;
import de.segoy.springboottradingibkr.client.service.accountsummary.profitandloss.AccountPnLService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/account-summary")
@RequiredArgsConstructor
public class AccountSummaryController {

    private final AccountSummaryService accountSummaryService;
    private final AccountPnLService accountPnLService;


    @GetMapping
    public void getAccountSummary(){
        accountSummaryService.getAccountSummary();
    }
    @GetMapping("/pnl")
    public void getPnL(){
        accountPnLService.getAccountPnL();
    }
    @GetMapping("/cancel")
    public void cancelAccountSummary(){
        accountSummaryService.cancelAccountSummary();
    }
    @GetMapping("/pnl/cancel")
    public void cancelPnL(){
        accountPnLService.cancelAccountPnL();
    }
}
