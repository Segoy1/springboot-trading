package de.segoy.springboottradingibkr.client.service.accountsummary.profitandloss;

import de.segoy.springboottradingdata.config.PropertiesConfig;
import de.segoy.springboottradingdata.model.entity.ProfitAndLossData;
import de.segoy.springboottradingdata.service.apiresponsecheck.AccountPnLApiResponseChecker;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountPnLService {

    private final AccountPnLApiCaller accountPnLApiCaller;
    private final CancelAccountPnLApiCaller cancelAccountPnLApiCaller;
    private final AccountPnLApiResponseChecker accountPnLApiResponseChecker;
    private final PropertiesConfig propertiesConfig;

    public AccountPnLService(AccountPnLApiCaller accountPnLApiCaller,
                             CancelAccountPnLApiCaller cancelAccountPnLApiCaller,
                             AccountPnLApiResponseChecker accountPnLApiResponseChecker,
                             PropertiesConfig propertiesConfig) {
        this.accountPnLApiCaller = accountPnLApiCaller;
        this.cancelAccountPnLApiCaller = cancelAccountPnLApiCaller;
        this.accountPnLApiResponseChecker = accountPnLApiResponseChecker;
        this.propertiesConfig = propertiesConfig;
    }

    public Optional<ProfitAndLossData> getAccountPnL() {
        cancelAccountPnLApiCaller.callApi();
        accountPnLApiCaller.callApi();
        return accountPnLApiResponseChecker.checkForApiResponseAndUpdate(propertiesConfig.getPnlAccountId());
    }
}
