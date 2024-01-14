package de.segoy.springboottradingweb.controller.restapicontroller;

import de.segoy.springboottradingdata.model.entity.AccountSummaryData;
import de.segoy.springboottradingdata.model.entity.ProfitAndLossData;
import de.segoy.springboottradingibkr.client.service.accountsummary.AccountSummaryService;
import de.segoy.springboottradingibkr.client.service.accountsummary.profitandloss.AccountPnLService;
import de.segoy.springboottradingweb.service.ResponseMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/account-summary")
public class AccountSummaryController {

    private final AccountSummaryService accountSummaryService;
    private final AccountPnLService accountPnLService;
    private final ResponseMapper responseMapper;

    public AccountSummaryController(AccountSummaryService accountSummaryService, AccountPnLService accountPnLService, ResponseMapper responseMapper) {
        this.accountSummaryService = accountSummaryService;
        this.accountPnLService = accountPnLService;
        this.responseMapper = responseMapper;
    }

    @GetMapping
    public ResponseEntity<List<AccountSummaryData>> getAccountSummary(){
        return responseMapper.mapResponse(accountSummaryService.getAccountSummary());
    }
    @GetMapping("/pnl")
    public ResponseEntity<ProfitAndLossData>getPnL(){
        return responseMapper.mapResponse(accountPnLService.getAccountPnL());
    }
}
