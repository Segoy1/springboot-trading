package de.segoy.springboottradingibkr.client.service.accountsummary.profitandloss;

import com.ib.client.EClientSocket;
import de.segoy.springboottradingdata.config.PropertiesConfig;
import de.segoy.springboottradingibkr.client.service.ApiCallerWithoutParameter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Qualifier("AccountPnLCancelApiCaller")
class AccountPnLCancelApiCaller implements ApiCallerWithoutParameter {

    private final EClientSocket client;
    private final PropertiesConfig propertiesConfig;

    public AccountPnLCancelApiCaller(EClientSocket client, PropertiesConfig propertiesConfig) {
        this.client = client;
        this.propertiesConfig = propertiesConfig;
    }

    @Override
    public void callApi() {
        client.cancelPnL(propertiesConfig.getPnlAccountId());
    }
}
