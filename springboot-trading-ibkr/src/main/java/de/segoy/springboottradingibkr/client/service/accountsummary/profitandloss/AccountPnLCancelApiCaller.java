package de.segoy.springboottradingibkr.client.service.accountsummary.profitandloss;

import com.ib.client.EClientSocket;
import de.segoy.springboottradingdata.config.PropertiesConfig;
import de.segoy.springboottradingibkr.client.service.ApiCallerWithoutParameter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service("AccountPnLCancelApiCaller")
@RequiredArgsConstructor
class AccountPnLCancelApiCaller implements ApiCallerWithoutParameter {

    private final EClientSocket client;
    private final PropertiesConfig propertiesConfig;

    @Override
    public void callApi() {
        client.cancelPnL(propertiesConfig.getPnlAccountId());
    }
}
