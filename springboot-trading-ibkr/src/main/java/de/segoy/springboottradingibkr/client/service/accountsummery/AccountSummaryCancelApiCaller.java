package de.segoy.springboottradingibkr.client.service.accountsummery;

import com.ib.client.EClientSocket;
import de.segoy.springboottradingdata.config.PropertiesConfig;
import de.segoy.springboottradingdata.model.entity.AccountSummaryData;
import de.segoy.springboottradingibkr.client.service.ApiCallerWithoutParameter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("AccountSummaryCancelApiCaller")
public class AccountSummaryCancelApiCaller implements ApiCallerWithoutParameter<AccountSummaryData> {

    private final EClientSocket client;
    private final PropertiesConfig propertiesConfig;

    public AccountSummaryCancelApiCaller(EClientSocket client, PropertiesConfig propertiesConfig) {
        this.client = client;
        this.propertiesConfig = propertiesConfig;
    }

    @Override
    public void callApi() {
        client.cancelAccountSummary(propertiesConfig.getACCOUNT_SUMMARY_ID());
    }
}
